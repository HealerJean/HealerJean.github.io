from sentence_transformers import SentenceTransformer

# 加载中文 embedding 模型（首次会自动下载，约 100MB）
model = SentenceTransformer('BAAI/bge-small-zh-v1.5')

# 输入任意中文
text = "我喜欢吃苹果。"

# 生成 embedding
embedding = model.encode(text)

print("向量长度：", len(embedding))  # 输出：512
print("前5个维度：", embedding[:5])