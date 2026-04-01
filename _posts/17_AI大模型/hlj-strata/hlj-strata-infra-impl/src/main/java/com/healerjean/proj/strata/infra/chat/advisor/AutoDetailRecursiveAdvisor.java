package com.healerjean.proj.strata.infra.chat.advisor;


import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * AI 回答太短，自动让它重新详细回答
 */
@Component
public class AutoDetailRecursiveAdvisor implements CallAdvisor {

    private final int maxRetries = 2;
    private int retryCount = 0;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 600;
    }

    @Override
    public String getName() {
        return "auto-detail-advisor";
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        ChatClientResponse response = chain.nextCall(request);

        // 获取AI回答
        String answer = response.chatResponse().getResults().get(0).getOutput().getText();

        // 如果太短 且 没超过重试次数 → 递归重试
        if (answer.length() < 50 && retryCount < maxRetries) {
            retryCount++;
            System.out.println("【递归】回答太短，让AI重新详细回答！次数：" + retryCount);

            // 构建新提示
            String newPrompt = request.prompt().getUserMessage().getText()
                    + "\n请详细回答，至少100字。";

            ChatClientRequest newRequest = request.mutate()
                    .prompt(new Prompt(newPrompt))
                    .build();

            // 递归！
            return chain.copy(this).nextCall(newRequest);
        }

        return response;
    }
}