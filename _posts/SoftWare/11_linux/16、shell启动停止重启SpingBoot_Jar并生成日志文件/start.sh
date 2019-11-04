#!/bin/bash
   echo starting
   java -jar admin-1.0-SNAPSHOT.jar --spring.profiles.active=ihaidou  > log.file 2>log.error &
