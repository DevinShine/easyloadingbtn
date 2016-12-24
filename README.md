This is a library project with a custom view that implements concept of Send Comment (https://dribbble.com/shots/1986254-Send-Comment-Shoppr) made by Nikosaurier for android


###Demo
![demo][1]

###Usage
Add the EasyLoadingBtn in your layout

```
<com.shadev.easyloadingdemo.view.LoadingButton
        android:id="@+id/lbtn_default"
        android:layout_centerInParent="true"
        android:layout_width="100dp"
        android:layout_height="100dp"/>
```

and
```
mDefaultLButton.setCallback(new LoadingButton.Callback() {
            @Override
            public void complete() {
                //TODO
            }
        });
```


  [1]: http://ww1.sinaimg.cn/mw690/9994fcfcgw1eqmpazqenmg20fo0l6wg5.gif
