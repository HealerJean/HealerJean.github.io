/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.build.compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * {@link Plugin} that applies conventions for compiling Java sources in Spring Framework.
 * <p>One can override the default Java source compatibility version
 * with a dedicated property on the CLI: {@code "./gradlew test -PjavaSourceVersion=11"}.
 *
 * @author Brian Clozel
 * @author Sam Brannen
 */
public class CompilerConventionsPlugin implements Plugin<Project> {

	/**
	 * The project property that can be used to switch the Java source
	 * compatibility version for building source and test classes.
	 */
	public static final String JAVA_SOURCE_VERSION_PROPERTY = "javaSourceVersion";

	public static final JavaVersion DEFAULT_COMPILER_VERSION = JavaVersion.VERSION_1_8;

	private static final List<String> COMPILER_ARGS;

	private static final List<String> TEST_COMPILER_ARGS;

	static {
		List<String> commonCompilerArgs = Arrays.asList(
				"-Xlint:serial", "-Xlint:cast", "-Xlint:classfile", "-Xlint:dep-ann",
				"-Xlint:divzero", "-Xlint:empty", "-Xlint:finally", "-Xlint:overrides",
				"-Xlint:path", "-Xlint:processing", "-Xlint:static", "-Xlint:try", "-Xlint:-options"
		);
		COMPILER_ARGS = new ArrayList<>();
		COMPILER_ARGS.addAll(commonCompilerArgs);
		COMPILER_ARGS.addAll(Arrays.asList(
				"-Xlint:varargs", "-Xlint:fallthrough", "-Xlint:rawtypes", "-Xlint:deprecation",
				"-Xlint:unchecked", "-Werror"
		));
		TEST_COMPILER_ARGS = new ArrayList<>();
		TEST_COMPILER_ARGS.addAll(commonCompilerArgs);
		TEST_COMPILER_ARGS.addAll(Arrays.asList("-Xlint:-varargs", "-Xlint:-fallthrough", "-Xlint:-rawtypes",
				"-Xlint:-deprecation", "-Xlint:-unchecked", "-parameters"));
	}

	@Override
	public void apply(Project project) {
		project.getPlugins().withType(JavaPlugin.class, javaPlugin -> applyJavaCompileConventions(project));
	}

	/**
	 * Applies the common Java compiler options for main sources, test fixture sources, and
	 * test sources.
	 * @param project the current project
	 */
	private void applyJavaCompileConventions(Project project) {
		JavaPluginConvention java = project.getConvention().getPlugin(JavaPluginConvention.class);
		if (project.hasProperty(JAVA_SOURCE_VERSION_PROPERTY)) {
			JavaVersion javaSourceVersion = JavaVersion.toVersion(project.property(JAVA_SOURCE_VERSION_PROPERTY));
			java.setSourceCompatibility(javaSourceVersion);
		}
		else {
			java.setSourceCompatibility(DEFAULT_COMPILER_VERSION);
		}
		java.setTargetCompatibility(DEFAULT_COMPILER_VERSION);

		project.getTasks().withType(JavaCompile.class)
				.matching(compileTask -> compileTask.getName().equals(JavaPlugin.COMPILE_JAVA_TASK_NAME))
				.forEach(compileTask -> {
					compileTask.getOptions().setCompilerArgs(COMPILER_ARGS);
					compileTask.getOptions().setEncoding("UTF-8");
				});
		project.getTasks().withType(JavaCompile.class)
				.matching(compileTask -> compileTask.getName().equals(JavaPlugin.COMPILE_TEST_JAVA_TASK_NAME)
						|| compileTask.getName().equals("compileTestFixturesJava"))
				.forEach(compileTask -> {
					compileTask.getOptions().setCompilerArgs(TEST_COMPILER_ARGS);
					compileTask.getOptions().setEncoding("UTF-8");
				});
	}

}
