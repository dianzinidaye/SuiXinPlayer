package com.example.suixinplayer.uitli;

import java.util.LinkedList;

public class SetList<T> extends LinkedList<T> {
    private static final long serialVersionUID = 1434324234L;


    public void noRepeatAdd(T object) {
        if (size() == 0) {
             super.add(object);
        } else {
            int count = 0;
            int i = 0;
            for (T t : this) {

                if (t.equals(object)) {

                    count++;
                    break;
                }
                i++;
            }
            if (count == 0) {
                super.addFirst(object);

            } else {
               super.remove(i);
               super.addFirst(object);
            }


        }
    }
}
