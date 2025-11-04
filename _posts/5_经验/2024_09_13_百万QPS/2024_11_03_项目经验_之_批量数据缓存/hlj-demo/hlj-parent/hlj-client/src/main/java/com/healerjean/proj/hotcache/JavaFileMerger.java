package com.healerjean.proj.hotcache;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JavaFileMerger {

    /**
     * 扫描指定目录下所有 .java 文件，合并到一个输出文件中，每个文件之间用 10 个空行分隔。
     *
     * @param sourceDir 源代码根目录（会递归扫描子目录）
     * @param outputFile 输出文件路径（如 merged_sources.java）
     * @throws IOException 文件读写异常
     */
    public static void mergeJavaFiles(String sourceDir, String outputFile) throws IOException {
        Path sourcePath = Paths.get(sourceDir);
        Path outputPath = Paths.get(outputFile);

        if (!Files.isDirectory(sourcePath)) {
            throw new IllegalArgumentException("源路径不是有效目录: " + sourceDir);
        }

        // 递归查找所有 .java 文件
        List<Path> javaFiles = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(sourcePath)) {
            javaFiles = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .sorted() // 可选：按路径排序
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }

        // 写入目标文件
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            String tenEmptyLines = "\n\n\n\n\n\n\n\n\n\n"; // 10 个空行

            for (int i = 0; i < javaFiles.size(); i++) {
                Path file = javaFiles.get(i);

                // 写入文件路径注释（便于追踪）
                writer.write("// ===== File: " + sourcePath.relativize(file).toString() + " =====\n");

                // 兼容 Java 8：使用 readAllBytes 替代 readString
                byte[] bytes = Files.readAllBytes(file);
                String content = new String(bytes, StandardCharsets.UTF_8);
                writer.write(content);

                // 如果不是最后一个文件，添加 10 个空行
                if (i < javaFiles.size() - 1) {
                    writer.write(tenEmptyLines);
                }
            }
        }

        System.out.println("✅ 合并完成！共处理 " + javaFiles.size() + " 个 Java 文件。");
        System.out.println("输出文件: " + outputPath.toAbsolutePath());
    }



    @Test
    public void test() throws IOException {
        String source = "/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/_posts/5_经验/2024_09_13_百万QPS/2024_11_03_项目经验_之_批量数据缓存/hlj-demo/hlj-parent/hlj-client/src/main/java/com/healerjean/proj/hotcache";
        String target = "/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/_posts/5_经验/2024_09_13_百万QPS/2024_11_03_项目经验_之_批量数据缓存/hlj-demo/hlj-parent/hlj-client/src/main/java/com/healerjean/proj/hotcache/code.java";
        mergeJavaFiles(source, target);
    }

}