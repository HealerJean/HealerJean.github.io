package com.hlj.arith.demo00126_课程表;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
作者：HealerJean
题目：
 现在你总共有 n 门课需要选，记为 0 到 n-1。
 在选修某些课程之前需要一些先修课程。 例如，想要学习课程 0 ，你需要先完成课程 1 ，我们用一个匹配来表示他们: [0,1]
 给定课程总量以及它们的先决条件，返回你为了学完所有课程所安排的学习顺序。
 可能会有多个正确的顺序，你只要返回一种就可以了。如果不可能完成所有课程，返回一个空数组。
     说明:
         输入的先决条件是由边缘列表表示的图形，而不是邻接矩阵。详情请参见图的表示法。
         你可以假定输入的先决条件中没有重复的边。
     提示:
         这个问题相当于查找一个循环是否存在于有向图中。如果存在循环，则不存在拓扑排序，因此不可能选取所有课程进行学习。
         通过 DFS 进行拓扑排序 - 一个关于Coursera的精彩视频教程（21分钟），介绍拓扑排序的基本概念。
         拓扑排序也可以通过 BFS 完成。
     示例 1:
         输入: 2, [[1,0]]
         输出: [0,1]
         解释: 总共有 2 门课程。要学习课程 1，你需要先完成课程 0。因此，正确的课程顺序为 [0,1] 。
     示例 2:
         输入: 4, [[1,0],[2,0],[3,1],[3,2]]
         输出: [0,1,2,3] or [0,2,1,3]
         解释: 总共有 4 门课程。要学习课程 3，你应该先完成课程 1 和课程 2。并且课程 1 和课程 2 都应该排在课程 0 之后。
              因此，一个正确的课程顺序是 [0,1,2,3] 。另一个正确的排序是 [0,2,1,3] 。
解题思路：
*/
public class 课程表_2 {

    @Test
    public void test(){
        int numCourses = 4 ;
        // int[][] prerequisites = {{2,1},{1,0}};
        // int[][] prerequisites = {{1,0},{2,0}};
        // int[][] prerequisites = {{1,0}};
        // int[][] prerequisites = {{1,0},{0,1}};
        // int[][] prerequisites = {{}};
        // int[][] prerequisites = {{1,0},{2,1}};

        int[][] prerequisites = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        System.out.println(Arrays.toString(findOrder(numCourses, prerequisites)));
    }




    // 栈下标 (要完成后面的，必须先完成前面的)
    int index;
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // 1、 存储有向图，每个点出发都是一个List，一共可能会有 numCourses个出发的点
        List<List<Integer>>   graph = new ArrayList<>();
        for (int i = 0; i < numCourses; ++i) {
            graph.add(new ArrayList<>());
        }

        // 2、 课程是从0到n-1的，也就是说。完成有向图初步工作
        // 要完成
        for (int[] line : prerequisites) {
            int  after = line[1];
            List<Integer> list = graph.get(after);

            int first = line[0];
            list.add(first);
        }


        // 3、标记每个节点的状态：0=未搜索，1=搜索中，2=已完成
        int[] visited  = new int[numCourses];
        // 4、用数组来模拟栈（拓扑排序好的数组），下标 n-1 为栈底，0 为栈顶, 每次挑选一个「未搜索」的节点，开始进行深度优先搜索
        int[] result  = new int[numCourses];
        //n-1 为栈底 ，从栈底开始放入
        index = numCourses - 1;
        for (int i = 0; i < numCourses; ++i) {
            //如果未搜索的话才搜索
            if (visited[i] == 0) {
                boolean flag =  dfs(i, graph, visited, result);
                if (flag) {
                    return new int[0];
                }
            }
        }

        // 如果没有环，那么就有拓扑排序
        return result;
    }

    /**
     * 深度优先搜索
     */
    public boolean dfs(int i, List<List<Integer>> graph, int[] visited, int[] result) {
        // 一搜索，将节点标记为「搜索中」
        visited[i] = 1;

        // 搜索其相邻节点 ，只要发现有环，立刻停止搜索
        for (int j : graph.get(i)) {
            // 如果「未搜索」那么搜索相邻节点
            if (visited[j] == 0) {
                boolean flag = dfs(j, graph, visited, result);
                if (flag) {
                    return true;
                }
                // 如果「搜索中」说明找到了环
            } else if (visited[j] == 1) {
                return true;
            }
        }

        // 先搜索完成的,肯定在有向图的末尾节点，也就是最后阅读
        result[index--] = i;

        // 将节点标记为「已完成」
        visited[i] = 2;
        return false;
    }
}
