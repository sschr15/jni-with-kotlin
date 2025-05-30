package com.sschr15;

public class NativeBackedIntArray implements AutoCloseable {
    static {
        System.loadLibrary("jnikt");
    }

    private final long nativePtr;
    private boolean exists = true;

    public NativeBackedIntArray(int size) {
        this.nativePtr = createNativeArray(size);
    }

    public NativeBackedIntArray(int[] array) {
        this.nativePtr = createFromArray(array);
    }

    private static native long createNativeArray(int size);
    private static native long createFromArray(int[] array);
    private static native boolean equals(long ptr1, long ptr2);
    private static native int get(long ptr, int index);
    private static native void set(long ptr, int index, int value);
    private static native int size(long ptr);
    private static native void clear(long ptr);
    private static native void fill(long ptr, int value);
    private static native void sort(long ptr);
    private static native void deleteNativeArray(long ptr);

    private void checkExists() {
        if (!exists) {
            throw new IllegalStateException("This NativeBackedIntArray has already been closed.");
        }
    }

    public int get(int index) {
        checkExists();
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
        return get(nativePtr, index);
    }
    
    public void set(int index, int value) {
        checkExists();
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
        set(nativePtr, index, value);
    }

    public int size() {
        checkExists();
        return size(nativePtr);
    }

    public void clear() {
        checkExists();
        clear(nativePtr);
    }

    public void fill(int value) {
        checkExists();
        fill(nativePtr, value);
    }

    public void sort() {
        checkExists();
        sort(nativePtr);
    }

    @Override
    public void close() {
        if (exists) {
            deleteNativeArray(nativePtr);
            exists = false;
        }
    }

    public int[] toArray() {
        checkExists();
        int[] array = new int[size()];
        for (int i = 0; i < size(); i++) {
            array[i] = get(i);
        }
        return array;
    }

    @Override
    public String toString() {
        checkExists();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size(); i++) {
            sb.append(get(i));
            if (i < size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        checkExists();
        int result = 1;
        for (int i = 0; i < size(); i++) {
            result = 31 * result + get(i);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        checkExists();
        if (this == obj) return true;
        if (!(obj instanceof NativeBackedIntArray other)) return false;
        return equals(this.nativePtr, other.nativePtr);
    }
}
