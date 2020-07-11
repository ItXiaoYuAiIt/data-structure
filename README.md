# data-structure
This project is for learning Java data structures
## learn ArrayList
### ArrayList源码分析
1. 打开ArrayList的源码我们能看到ArrayList实现了List接口，扩展至AbstractList，其本质是一个可变长度的数组。
2. Java8中ArrayList包含注释一起一共1468行代码，算是一个比较复杂的类，所以这当中一定有值得我们研究的东西。
3. 根据面向对象的基本原则与特性，对象都有自己的属性（成员变量）和行为（方法），所以我们先来看一看ArrayList的属性即成员变量。

```java
private static final long serialVersionUID = 8683452581122892189
    
/**
 * Default initial capacity.
 * 默认的初始容量
 */
private static final int DEFAULT_CAPACITY = 1
    
/**
 * Shared empty array instance used for empty instances.
 * 用于空实例的共享空数组实例。
 */
private static final Object[] EMPTY_ELEMENTDATA = {}

/**
 * Shared empty array instance used for default sized empty instances. We
 * distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
 * first element is added.
 * 用于默认大小的空实例的共享空数组实例。我们将其与EMPTY_ELEMENTDATA区分开来，
 * 以便知道何时扩容添加第一个元素。
 */
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {}

/**
 * The array buffer into which the elements of the ArrayList are stored.
 * The capacity of the ArrayList is the length of this array buffer. Any
 * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
 * will be expanded to DEFAULT_CAPACITY when the first element is added.
 * 存储ArrayList元素的数组。
 * ArrayList的容量是这个数组的长度。
 * 当添加第一个元素时，将扩展DEFAULT_CAPACITY个长度
 */
transient Object[] elementData; // non-private to simplify nested class acce
    
/**
 * The size of the ArrayList (the number of elements it contains).
 * 这是值是数据组中元素的格式
 * @serial
 */
private int size;
```

4. 通过上述源代码，我们很容易发现ArrayList就是一个可变长度的数组，我们对ArrayList的操作实际上就是对elementData数组的操作，ArrayList中的元素实际上是存储在elementData数组当中的，size是用来记录数组中元素个数的。既然知道了ArrayList是一个可变长度的数组，接着就到了我们研究的重点看看**ArrayList是如何实现自动扩容的**？
5. 继续依据面向对象的特性，对象都有自己的属性（成员变量）和行为（方法），我们来看看ArrayList的行为，既然研究的重点是扩容我们就先看看add()方法。

```java
/**
 * Appends the specified element to the end of this list.
 *
 * @param e element to be appended to this list
 * @return <tt>true</tt> (as specified by {@link Collection#add})
 * 往列表尾部添加元素
 */
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}
```

6. 看到add(E e)方法的doc注释，视乎没太大的意义，只是告诉我们这个方法时往集合尾部添加元素的 //add 添加
7. 接着我们看ensureCapacityInternal()方法。//ensure Capacity Internal 确保内部容量

```java
private void ensureCapacityInternal(int minCapacity) {
    ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
}
```

8. what这个方法居然没有注释，这ArrayList源码作者的编程习惯也太不规范了吧，看来这水平跟我也差不多。哈哈其实这样想你就错了，大神的代码就是大神的代码。人家这个变量名称**minCapacity（最小容量）**实在是用的太好了.我们结合之前add()方法的代码，在调用这个方法时传入的是**size+1**,也就是说我们的数组容量最小得有size+1,这的变量名实在是太妙了。
9. 接着我们看calculateCapacity()方法。 //calculate Capacity 计算容量

```java
private static int calculateCapacity(Object[] elementData, int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        return Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    return minCapacity;
}
```

10. 什么玩意啊！居然又没注释，这什么鬼源码，这编码习惯简直是稀烂还不如我了（ps:其实依据编码规约，对于private方法是不需要注释的，因为private方法仅内部使用，作者肯定知道他是干嘛的，而对于public等对外提供的方法则需要严格书写doc注释，要不然其他人怎么使用你的方法）。继续分析代码calculateCapacity方法需要两个参数elementData（存储元素的数组）和minCapacity（最小容量），代码第一行逻辑为if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA)，这个时候我们需要去看一看ArrayList的构造函数。

```java
/**
 * Constructs an empty list with an initial capacity of ten.
 */
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}
```

11. 通过上述代码我们看到，ArrayList的无参构造就是将DEFAULTCAPACITY_EMPTY_ELEMENTDATA复制给elementData。所以再回到calculateCapacity方法的代码，此时elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA逻辑成立且elementData={}、minCapacity=1。接着我们继续看代码return Math.max(DEFAULT_CAPACITY, minCapacity)，因为DEFAULT_CAPACITY=10大于minCapacity=1，所以此时Math.max返回DEFAULT_CAPACITY。
12. 回到ensureCapacityInternal方法我们一起看ensureExplicitCapacity()方法。//ensure Explicit Capacity 确保正确的容量。

```java
private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
```

13. what`s wrong怎么又没有注释呢，嗨！垃圾这编码水平，还好意思写jdk（ps:再次强调private方法是可以不写注释的，而且这是符合编码规范的）。算了还是继续看代码吧，此时参数minCapacity=DEFAULT_CAPACITY=10，第一行代码modCount++，modCount编码位于AbstractList中源码如下。

```java
/**
 * The number of times this list has been <i>structurally modified</i>.
 * Structural modifications are those that change the size of the
 * list, or otherwise perturb it in such a fashion that iterations in
 * progress may yield incorrect results.
 *
 * <p>This field is used by the iterator and list iterator implementation
 * returned by the {@code iterator} and {@code listIterator} methods.
 * If the value of this field changes unexpectedly, the iterator (or list
 * iterator) will throw a {@code ConcurrentModificationException} in
 * response to the {@code next}, {@code remove}, {@code previous},
 * {@code set} or {@code add} operations.  This provides
 * <i>fail-fast</i> behavior, rather than non-deterministic behavior in
 * the face of concurrent modification during iteration.
 *
 * <p><b>Use of this field by subclasses is optional.</b> If a subclass
 * wishes to provide fail-fast iterators (and list iterators), then it
 * merely has to increment this field in its {@code add(int, E)} and
 * {@code remove(int)} methods (and any other methods that it overrides
 * that result in structural modifications to the list).  A single call to
 * {@code add(int, E)} or {@code remove(int)} must add no more than
 * one to this field, or the iterators (and list iterators) will throw
 * bogus {@code ConcurrentModificationExceptions}.  If an implementation
 * does not wish to provide fail-fast iterators, this field may be
 * ignored.
 */
protected transient int modCount = 0;
```

14. 这个注释多我喜欢，可是这吧啦吧啦说一堆到底是个啥呢，英语水平有限我的理解就是**modCount是用于记录ArrayList的扩容次数**
15. 好了再去看ensureExplicitCapacity方法吧，modCount++之后就进入了代码块

```java
// overflow-conscious code
if (minCapacity - elementData.length > 0)
    grow(minCapacity);
```

16. 哈哈这个方法有意思，还有方法内注释，这个是真跟我有点像（其实这样是不太规范的，规整的代码，仅仅只需要靠变量名以及方法名，就能让人通俗易懂的读懂代码，过多的方法内注释是不规范不可取的，当然这里只有一行）。因为此时minCapacity=10、elementData.length=0，所有minCapacity - elementData.length > 0成立，进入grow(minCapacity)方法（ps:这个方法名也起得好啊，扩容，所以其实大神上面那个单行注释压根不用要）。
17. 我们来一起看看grow()方法，唉！不容易啊，终于进入正题了。//grow 扩展

```java
/**
 * Increases the capacity to ensure that it can hold at least the
 * number of elements specified by the minimum capacity argument.
 *
 * @param minCapacity the desired minimum capacity
 */
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

18. 咦！不错、不错。这个私有的方法也有注释，这个注释的意思呢，大概就是：扩展容量以确保至少能容纳minCapacity个元素。接下来我们一起来分析代码：

    1. *int oldCapacity = elementData.length* 此时因为elementData为一个空数组，所以oldCapacity=0。
    2. *int newCapacity = oldCapacity + (oldCapacity >> 1)* 因为oldCapacity=0，所以此时newCapacity=0。
    3. *if (newCapacity - minCapacity < 0) newCapacity = minCapacity* 因为此时newCapacity=0、minCapacity=10，所以最终得到newCapacity=10
    4. *if (newCapacity - MAX_ARRAY_SIZE > 0) newCapacity = hugeCapacity(minCapacity)* 因为newCapacity <MAX_ARRAY_SIZE，所以直接进入*elementData = Arrays.copyOf(elementData, newCapacity)*。这里呢就不对Arrays.copyOf()，因为他最终会调用System类的arraycopy()方法，该方法为一个native方法，说明这个方法是用其他语言写的本地方法，我们如果想查看源码就必须通过[openjdk8](http://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/4687075d8ccf/src/share/native/java/lang/System.c)查看相关源码了。

    + **补充说明**

    ​      *int newCapacity = oldCapacity + (oldCapacity >> 1)*，这里涉及到一个位移运算，算是比较经典吧，所以补充说明一下。第一次扩容时因为oldCapacity=0，所以oldCapacity向右移动一位还是0。如果是第二次扩容oldCapacity为10，则oldCapacity >> 1=5，扩容后newCapacity=15。所以每次扩容的维度，其实是原来容量的1/2。

    ​      计算过程如下二进制10(01010,前面高位省略)，向右移动一位得到00101二进制的5，如果大家对二进制还是不太熟悉，可以阅读我之前的博文[重走Java路-基本数据类型](https://blog.csdn.net/CrazyJavaWu/article/details/105801074)上面有更详细的对二进制的介绍。

19. 上面已经大致分析完了ArrayList的扩容机制，所有现在就有了我们面试中经常见到的问题：为什么ArrayList查询快添加插入元素却慢呢？分析完源代码之后我相信我们大家都能很快的回答出这个问题，因为ArrayList是基于数组进行元素存储的，从数组中获取元素对CPU而言只是一个取指令、存指令的操作，他的时间复杂度为O(1)，而对于插入操作会引起数组的复制操作所以相对较慢。

### 简单实现MyArrayList
MyArrayList只是一个非常简单的ArrayList，也只实现了size(),add()等几个常用的方法，旨在弄清楚ArrayList的数据结构，扩容机制的知识，[源码地址](https://github.com/ItXiaoYuAiIt/data-structure)。

```java
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

```



