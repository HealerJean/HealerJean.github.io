package com.healerjean.proj.strata.infra.mcp.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Llama3Controller
 *
 * @author zhangyujin
 * @date 2025/11/18
 */
@Slf4j
@RestController("/mcp")
public class BookMcpServer {

    /**
     * 普通对话
     *
     * @Tool 使用这个注解标注就相当于对外暴露对应的能力，MCP Client初始化的时候会收集好目标MCP Server具备哪些能力，并把这些能力告诉LLM大模型，作为大模型的工具库。比如这个MCP Server就可以查询指定城市的天气；
     * @ToolParam 这个注解就是告诉MCP Client调用这个功能需要传什么参数
     */
    @Tool(name = "findBooksByTitle", description = "根据书名模糊查询图书，支持部分标题匹配")
    public List<String> findBooksByTitle(@ToolParam(description = "书名关键词") String title) {
        ArrayList<String> books = new ArrayList<>();
        books.add("语文书");
        books.add("数学书");
        books.add("英语书");
        return books.stream().filter(book -> book.contains(title)).toList();
    }

    @Tool(name = "findBooksByAuthor", description = "根据作者精确查询图书，返回书籍")
    public List<String> findBooksByAuthor(@ToolParam(description = "作者姓名") String author) {
        ArrayList<String> books = new ArrayList<>();
        books.add("张三丰");
        books.add("张无忌");
        books.add("赵敏");
        return books.stream().filter(book -> book.contains(author)).toList();
    }

    @Tool(name = "findBooksByCategory", description = "根据图书分类精确查询图书，返回书籍")
    public List<String> findBooksByCategory(@ToolParam(description = "图书分类") String category) {
        ArrayList<String> books = new ArrayList<>();
        books.add("水果");
        books.add("蔬菜");
        books.add("肉类");
        return books.stream().filter(book -> book.contains(category)).toList();
    }


}