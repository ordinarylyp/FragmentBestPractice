package lyp.com.fragmentbestpractice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsContentFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.news_content_frag,container,false);
        return view;
    }

    //将新闻的标题和内容显示在界面上
    public void refresh(String newsTitle,String newsContent){
        View visibilitylayout=view.findViewById(R.id.visibility_layout);
        visibilitylayout.setVisibility(View.VISIBLE);
        TextView newsTitleText= view.findViewById(R.id.news_title);
        TextView newsContentText=  view.findViewById(R.id.news_content);
        newsTitleText.setText(newsTitle); //刷新新闻的标题
        newsContentText.setText(newsContent);//刷新新闻的内容
    }
}
