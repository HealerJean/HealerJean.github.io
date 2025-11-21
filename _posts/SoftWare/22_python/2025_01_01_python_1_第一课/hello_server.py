# -*- coding: utf-8 -*-
"""
一个最简 HTTP 服务器示例，用于本地开发或演示。
注意：此服务器基于 Python 标准库 http.server，
      官方明确说明：不要在生产或预发布（pre-production）环境中使用！
      仅适用于本地测试、学习或快速原型验证。
"""

# 导入标准库中的 HTTP 服务器类和请求处理器基类
from http.server import HTTPServer, BaseHTTPRequestHandler


class HelloHandler(BaseHTTPRequestHandler):
    """
    自定义 HTTP 请求处理器，继承自 BaseHTTPRequestHandler。
    重写 do_GET 方法以处理客户端的 GET 请求。
    """

    def do_GET(self):
        # 发送 HTTP 响应状态码 200（表示请求成功）
        self.send_response(200)

        # 设置响应头：内容类型为纯文本，字符编码为 UTF-8
        # 这样可确保浏览器正确解析文本（包括中文等非 ASCII 字符）
        self.send_header("Content-type", "text/plain; charset=utf-8")

        # 结束响应头部分，准备发送响应体
        self.end_headers()

        # 向客户端写入响应体（必须是字节 bytes，因此需对字符串进行 UTF-8 编码）
        self.wfile.write("Hello, World!".encode("utf-8"))


if __name__ == "__main__":
    """
    主程序入口：仅在直接运行本脚本时启动服务器。
    若被其他模块 import，则不会自动启动。
    """

    try:
        # 创建 HTTP 服务器实例：
        #   - 监听地址建议使用 "127.0.0.1" 而非 "localhost"，
        #     避免因系统 hosts 配置问题导致 socket.gaierror 错误。
        #   - 端口设为 8000（可自定义，但需确保未被占用）
        #   - 使用自定义的 HelloHandler 处理请求
        server = HTTPServer(("127.0.0.1", 8000), HelloHandler)

        # 打印启动提示信息
        print("服务器启动在 http://127.0.0.1:8000")

        # 启动服务器并持续监听请求（阻塞式运行）
        server.serve_forever()

    except KeyboardInterrupt:
        # 捕获用户按 Ctrl+C 的中断信号，优雅关闭服务器
        print("\n检测到中断信号，正在关闭服务器...")
        server.server_close()
        print("服务器已关闭。")