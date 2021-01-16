package com.hlj.arith.demo00126_课程表;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
作者：HealerJean
题目：
 你这个学期必须选修 numCourse 门课程，记为 0 到 numCourse-1 。
 在选修某些课程之前需要一些先修课程。 例如，想要学习课程 0 ，你需要先完成课程 1 ，我们用一个匹配来表示他们：[0,1]
 给定课程总量以及它们的先决条件，请你判断是否可能完成所有课程的学习？
     提示：
         输入的先决条件是由 边缘列表 表示的图形，而不是 邻接矩阵 。详情请参见图的表示法。
         你可以假定输入的先决条件中没有重复的边。
         1 <= numCourses <= 10^5
     示例 1:
         输入: 2, [[1,0]]
         输出: true
         解释: 总共有 2 门课程。学习课程 1 之前，你需要完成课程 0。所以这是可能的。
     示例 2:
         输入: 2, [[1,0],[0,1]]
         输出: false
         解释: 总共有 2 门课程。学习课程 1 之前，你需要先完成​课程 0；并且学习课程 0 之前，你还应先完成课程 1。这是不可能的。
解题思路：
*/
public class 课程表_1 {

    @Test
    public void test(){
        int numCourses = 2 ;
        // int[][] prerequisites = {{2,1},{1,0}};
        // int[][] prerequisites = {{1,0},{2,0}};
        int[][] prerequisites = {{0,1}};
        // int[][] prerequisites = {{1,0},{0,1}};
        // int[][] prerequisites = {{}};
        // int[][] prerequisites = {{1,0},{2,1}};
        // int[][] prerequisites = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        System.out.println((canFinish(numCourses, prerequisites)));
    }


    // 栈下标 (要完成后面的，必须先完成前面的)
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        if (prerequisites.length == 0) {
            return true;
        }

        List<List<Integer>> graph = new ArrayList<>();
        //定义从节点出发的有向图
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        // 制作有向图
        for (int[] line : prerequisites) {
            graph.get(line[1]).add(line[0]);
        }

        // 0 未搜索，1 搜索中， 2 搜索完成
        int[] visited = new int[numCourses];
        for (int i = 0; i < graph.size(); i++) {
            if (visited[i] == 0) {
                boolean flag = dfs(i, graph, visited);
                if (flag) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean dfs(int i, List<List<Integer>> graph, int[] visited) {
        //进来就表示要搜索了
        visited[i] = 1;
        for (int j : graph.get(i)) {
            if (visited[j] == 0) {
                boolean flag = dfs(j, graph, visited);
                if (flag) {
                    return true;
                }
                //如果是搜索中，则表示是闭环的
            } else if (visited[j] == 1) {
                return true;
            }
        }
        visited[i] = 2;
        return false;
    }
}
