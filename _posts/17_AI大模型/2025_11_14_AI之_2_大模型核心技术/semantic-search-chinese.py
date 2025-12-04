from sentence_transformers import SentenceTransformer
import numpy as np

# 加载本地中文模型
model = SentenceTransformer('BAAI/bge-small-zh-v1.5')

documents = [
    "猫是一种可爱的宠物，喜欢抓老鼠。",
    "狗是人类最好的朋友，忠诚又聪明。",
    "苹果公司发布了新款 iPhone。",
    "我昨天在公园散步，看到一只小猫。"
]

def get_embedding(text):
    return model.encode(text)

def cosine_sim(a, b):
    return np.dot(a, b) / (np.linalg.norm(a) * np.linalg.norm(b))

# 后续逻辑完全一样...
docs_embeddings = [get_embedding(doc) for doc in documents]
query = "我想养一只猫，有什么建议？"
query_embedding = get_embedding(query)
scores = [cosine_sim(query_embedding, doc_emb) for doc_emb in docs_embeddings]
best_idx = int(np.argmax(scores))
print("最相关文档：")
print(documents[best_idx])
print("相似度：", scores[best_idx])