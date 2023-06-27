package com.healerjean.proj.utils.bigdata;

/**
 * 并发工具类-结果类
 */
public class ParallelResult<R> {
    /**
     * index
     */
    private Long index;
    /**
     * data
     */
    private R data;

    /**
     * ParallelResult
     */
    public ParallelResult() {
    }

    /**
     * ParallelResult
     *
     * @param index index
     * @param data  data
     */
    public ParallelResult(Long index, R data) {
        this.index = index;
        this.data = data;
    }

    /**
     * of
     *
     * @param index index
     * @param data  data
     * @return {@link ParallelResult<R>}
     */
    public static <R> ParallelResult<R> of(Long index, R data) {

        return new ParallelResult<>(index, data);
    }

    /**
     * empty
     *
     * @return {@link ParallelResult<R>}
     */
    public static <R> ParallelResult<R> empty() {

        return new ParallelResult<>();
    }

    /**
     * isEmpty
     *
     * @return {@link boolean}
     */
    public boolean isEmpty() {
        return index == null && data == null;
    }

    public Long getIndex() {
        return index;
    }

    public R getData() {
        return data;
    }
}
