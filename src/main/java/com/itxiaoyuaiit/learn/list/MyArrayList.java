package com.itxiaoyuaiit.learn.list;

/**
 * @ClassName MyArrayList
 * @Description 这是一个自定义的简单的ArrayList，仅实现了add、get、indexOf等方法。其目的是为了弄懂ArrayList的自动扩容获取元素，添加元素等实现机制
 * @Author wuyuqing
 * @Date 2020/7/9 21:18
 * @Version 1.0
 */
public class MyArrayList<E> implements MyList {
    /**
     * 记录集合长度(集合中元素的个数)
     */
    private int size;

    /**
     * object类型的数组用于存储集合中的元素，初始容量为16
     */
    private Object[] elements = new Object[16];


    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean add(Object o) {
        // 判断集合长度是否等于数组长度
        if (elements.length > size) {
            elements[size] = o;
        } else {
            //进行数组扩容，扩容为原来的两倍
            Object[] temporary = new Object[elements.length * 2];
            //将原来数组中的元素拷贝到临时数组中
            for (int i = 0; i < elements.length; i++) {
                temporary[i] = elements[i];
            }
            //将新的元素添加到临时数组,并把临时数组赋值给elements
            temporary[size] = o;
            elements = temporary;
        }
        size += 1;
        return true;
    }

    @Override
    public boolean remove(int index) {
        boolean result = true;
        // 首先判断index的合法性
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }else if(index == size -1){//如果移除的是最后一位，则直接将该索引位置元素置null，size减1
            elements[index] = null;
            size -= 1;
        }else {
            // 移除指定索引下的元素，实际上是把index后面的元素前移一位
            for (int i = index; i < elements.length - 1; i++) {
                elements[i] = elements[i + 1];
            }
            size -= 1;
        }
        return result;
    }

    @Override
    public Object get(int index) {
        //首先判断索引的合法性，不合法返回null
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }else {
            return elements[index];
        }
    }

    @Override
    public Object set(int index, Object element) {
        //首先判断索引的合法性，不合法返回null
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }else {
            Object oldElement = elements[index];
            elements[index] = element;
            return oldElement;
        }
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if(elements[i].equals(o)){
                return i;
            }
        }
        return -1;
    }
}
