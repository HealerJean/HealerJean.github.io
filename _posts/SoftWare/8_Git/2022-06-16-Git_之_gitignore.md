---
title: Git_之_gitignore
date: 2022-12-12 00:00:00
tags: 
- Git
category: 
- Git
description: Git_之_gitignore
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





```
# Gradle
build
.gradle

testdata/
# Java gitignore #
.class
.log

# Package Files #
*.war
*.ear

# maven gitignore#
target/**

.svn/

# intelliJ.gitignore #
.idea
*.iml
*.ipr
*.iws

# Eclipse git ignore#
*.pydevproject
.project
.metadata
bin/**
target/**
*/bin/**
*/target/**
tmp/**
tmp/**/*
configuration/**
*.tmp
*.bak
*.orig
*.swp
*~.nib
.classpath
.settings/
.loadpath
.fileTable*
.cache
pom.xml.versionsBackup

# External tool builders
.externalToolBuilders/

# Locally stored "Eclipse launch configurations"
*.launch

# CDT-specific
.cproject

# PDT-specific
.buildpath

#log
*.log
*.log.*

# Windows Thumbs.db
*.db

# OSX
.DS_Store

# sass gitignore#
.sass-cache
.idea

# tcc_coverage
coverage.ec



config.client.*

temp/
*.pid
*.orig

hsf.configuration/

# code coverage report
*.ec



```





![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)



<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">

<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: '26SNdPq17LFEojns',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



