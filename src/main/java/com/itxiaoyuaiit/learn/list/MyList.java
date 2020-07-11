package com.itxiaoyuaiit.learn.list;

public interface MyList<E> {

    /**
     * 用于返回集合的长度（集合中元素的个数）
     * @return 返回集合长度
     */
    int size();

    /**
     * 用于添加元素
     *
     * @param e 待添加的元素
     * @return 元素是否添加成功
     */
    boolean add(E e);

    /**
     * 用于移除集合中指定位置的集合
     * @param index 索引下标
     * @return 元素是删除成功
     */
    boolean remove(int index);

    /**
     * 获取指定索引下的元素
     *
     * @param index 索引下标
     * @return 元素
     */
    E get(int index);

    /**
     * 覆盖指定索引下的元素
     *
     * @param index 索引下标
     * @param element 待添加的元素
     * @return 原来该下标下的元素
     */
    E set(int index, E element);

    /**
     * 获取指定元素的索引信息
     *
     * @param e 指定元素
     * @return 返回指定元素索引出现的一个位置，没有返回-1
     */
    int indexOf(E e);





}
