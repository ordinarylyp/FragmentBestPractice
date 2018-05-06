FragmentBestPractice
===================================
### android碎片探究
碎片（Fragment）是一种可以嵌入在活动当中的 UI 片段，它能让程序更加合理和充分 地利用大屏幕的空间，因而在平板上应用的非常广泛。使用碎片可以使平板的空间利用率更高，例如一个新闻类的应用，如图所示：
![fragment.png](/img/fragment.png "fragment")
### 创建碎片
新建一个左侧碎片布局 fragment_left.xml，代码如下所示：

 	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
     android:layout_width="match_parent"
   	 android:layout_height="match_parent"
   	 android:orientation="vertical">
	 <Button
	 	android:id="@+id/button"
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:layout_gravity="center_horizontal"
		android:text="按钮"/>

	</LinearLayout>
这个布局非常简单，只放置了一个按钮，并让它水平居中显示。然后新建右侧碎片布局fragment_right.xml，代码如下所示：

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    	android:layout_width="match_parent"
    	android:layout_height="match_parent"
    	android:orientation="vertical"
    	android:background="#00ff00">

    	<TextView
        	android:layout_width="wrap_content"
        	android:layout_height="wrap_content"
        	android:layout_gravity="center_horizontal"
        	android:textSize="20sp"
        	android:text="这是右边的fragment"/>

	</LinearLayout>
可以看到，我们将这个布局的背景色设置成绿色，并放置了一个 TextView 用于显示一段文本。

接着新建一个 LeftFragment 类，继承自 Fragment。注意，这里可能会有两个不同包 下的 Fragment 供你选择，一个是系统内置的android.app.Fragment，一个是 support-v4 库中的 android.support.v4.app.Fragment 。这里强烈建议使用 support-v4 库中的 Fragment，因为它可以让碎片在所有 Android 系统版本中保持功能一致性。

LeftFragment 的代码如下所示：
```Java
public class LeftFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, container, false);
        return view;
    }
}
```
这里仅仅是重写了 Fragment 的 onCreateView()方法，然后在这个方法中通过 LayoutInflater 的 inflate()方法将刚才定义的 fragment_left 布局动态加载进来，整个方法简单明了。接着我们用同样的方法再新建一个 RightFragment：
```Java
public class RightFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        return view;
    }
}
```
接下来修改 activity_fragment.xml 中的代码，如下所示：

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    	android:id="@+id/activity_fragment"
    	android:layout_width="match_parent"
    	android:layout_height="match_parent"
    	android:orientation="horizontal">

    	<fragment
        	android:id="@+id/fragment_left"
        	android:name="com.wonderful.myfirstcode.inquiry_fragment.LeftFragment"
        	android:layout_width="0dp"
        	android:layout_height="match_parent"
        	android:layout_weight="1"/>

    	<fragment
		android:id="@+id/fragment_right"
        	android:name="com.wonderful.myfirstcode.inquiry_fragment.RightFragment"
        	android:layout_width="0dp"
        	android:layout_height="match_parent"
        	android:layout_weight="1"/>
    
	</LinearLayout>
可以看到，我们使用了<fragment>标签在布局中添加碎片，通过 android:name 属性来显式指明要添加的碎片类名，注意一 定要将类的包名也加上。
最终的运行结果是一个左右各占据了半个屏幕的界面，分别是两个碎片。
  
### 碎片的生命周期
 每个活动在其生命周期内可能会有四种状态：运行状态、暂停 状态、停止状态和销毁状态。类似地，每个碎片在其生命周期内也可能会经历这几种状态，只不过在一些细小的地方会有部分区别。

###### 运行状态：
  当一个碎片是可见的，并且它所关联的活动正处于运行状态时，该碎片也处于运行状态。

###### 暂停状态：
  当一个活动进入暂停状态时（由于另一个未占满屏幕的活动被添加到了栈顶），与它相关联的可见碎片就会进入到暂停状态。

###### 停止状态：
  当一个活动进入停止状态时，与它相关联的碎片就会进入到停止状态。或者通过调用 FragmentTransaction 的 remove()、replace()方法将碎片从活动中移除，但若在事务提交之前调用 addToBackStack() 方法，这时的碎片也会进入到停止状态。总的来说，进入停止状态的碎片对用户来说是完全不可见的，有可能会被系统回收。

###### 销毁状态：
  碎片总是依附于活动而存在的，因此当活动被销毁时，与它相关联的碎片就会进入到销毁状态。或者通过调用 FragmentTransaction 的 remove()、replace()方法将碎片从活动中移除，但在事务提交之前并没有调用 addToBackStack()方法，这时的碎片也会进入 到销毁状态。

和活动 Acitvity 相似，Fragment 类中也提供了一系列的回调方法，以覆盖碎片生命周期的每个环节。其中，活动中有的回调方法，碎片中几乎都有，不过碎片还提供了一些附加的回调方法，重点来看下这几个回调：

##### onAttach() 当碎片和活动建立关联的时候调用。
##### onCreateView() 为碎片创建视图（加载布局）时调用。
##### onActivityCreated() 确保与碎片相关联的活动一定已经创建完毕的时候调用。
##### onDestroyView() 当与碎片关联的视图被移除的时候调用。
##### onDetach() 当碎片和活动解除关联的时候调用。

生命周期图如下：

![fragment_lifecycle.png ](/img/fragment_lifecycle.png "fragment_lifecycle")
### 一个简易的新闻应用（平板和手机共通）
要制作一个新闻应用，是需要使用到滚动控件的，滚动控件如何使用，可以看项目[RecyclerViewTest](https://github.com/ordinarylyp/the_third_chapter)

制作一个新闻应用，首先定义一个类[News](/app/src/main/java/lyp/com/fragmentbestpractice/News.java),然后新建一个布局[news_content_frag]()，用于作为新闻内容的布局。这个布局很简单，主要分为两个部分，头部显示新闻的标题，正文部分显示新闻的内容，中间用一条细线隔开。同时，在布局的最左边也加上一条细线（在平板使用时将右边显示的新闻标题隔开）。

再新建一个类[NewsContentFragment](/app/src/main/java/lyp/com/fragmentbestpractice/NewsContentFragment.java),这里加载了我们刚刚新建的news_content_frag布局。同时提提供了一个refresh()方法，用于将新闻的标题和内容显示在界面上。

但这样是不够的，这都是在双页模式下使用的，想要兼容手机，在单页模式下使用，我们还要新建一个活动[NewsContentActivity](/app/src/main/java/lyp/com/fragmentbestpractice/NewsContentActivity.java),还有这个活动的布局[news_content.xml](/app/src/main/res/layout/news_content.xml);在onCreate方法中先通过intent获取传入的新闻标题和内容，再通过FragmentManager的findFragmentById找到NewsConcentFragment的实例，调用refresh()方法将新闻标题和内容传入，就可以将数据显示出来了。

接下来还需再创建显示新闻列表的布局 [news_title_frag.xml](/app/src/main/res/layout/news_title_frag.xml)，这是用于显示新闻的标题的，这里就只有一个RecyclerView控件，所以还要新建一个[news_item.xml](/app/src/main/res/layout/new_item.xml)布局作为RecyclerView的子项。

新闻标题的列表和子项的布局建立好之后，我们需要一个用于展示的地方，新建一个类[NewsTitleFragment](/app/src/main/java/lyp/com/fragmentbestpractice/NewsTitleFragment.java)作为展示列表的碎片，修改主布局[activity_main.xml]()，在单页模式下只会加载一个新闻标题碎片；在res目录下新建layout-sw600dp文件夹，在这个文件夹下新建一个[activity_main.xml]()，表示在双页模式下回加载两个碎片，并把新闻内容的碎片放在了FrameLayout布局下，这个布局的id是news_content_layout。因此能知道这个布局就是双页模式，不能找到就是单页模式。
```Java
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.news_content_layout)!=null){
            isTwoPane=true;   //可以找到news_content_layout布局时，为双页模式。
        }else {
            isTwoPane=false;
        }
    }

````

接下来，就是要通过RecyclerView将新闻标题列表展示出来，所以我们需要一个适配器（adapter），我们在NewsContentActivity](/app/src/main/java/lyp/com/fragmentbestpractice/NewsContentActivity.java)中建一个内部类NewsAdapter：
```Java
    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
        private List<News> mNewsList;

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView newsTitleText;

            public ViewHolder(View view){
                super(view);
                newsTitleText=view.findViewById(R.id.news_title);
            }
        }
        public  NewsAdapter(List<News> newsList){
            mNewsList=newsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news=mNewsList.get(holder.getAdapterPosition());
                    if(isTwoPane){
                        NewsContentFragment newsContentFragment=(NewsContentFragment) getFragmentManager()
                                .findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(news.getTitle(),news.getContent());
                    }else {
                        NewsContentActivity.actionStart(getActivity(),news.getTitle(),news.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            News news=mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }
```
如果是双页模式，就刷新NewsContentFragment中的内容；如果是单页模式，就启动NewsContentActivity。

最后向RecyclerView中填充数据（同样在NewsTitleFragment中进行）
```Java
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.news_title_frag,container,false);
        RecyclerView newsTitleRecyclerView = view.findViewById(R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        newsTitleRecyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(getNews());
        newsTitleRecyclerView.setAdapter(adapter);
        return view;
    }
    
    private List<News> getNews(){
        List<News> newsList =new ArrayList<>();
        for (int i=1;i<=50;i++){
            News news =new News();
            news.setTitle("新闻标题 "+i);
            news.setContent(getRandomLengthContent("这是新闻内容哈"+i+"!"));
            newsList.add(news);
        }
        return newsList;
    }

    private String getRandomLengthContent(String content){
        Random random=new Random();
        int length = random.nextInt(20)+1;
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<length;i++){
            builder.append(content);
        }
        return builder.toString();
    }

```
  
