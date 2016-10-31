package fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.jufan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/10/29.
 */
public class ShouyeFragMent extends Fragment {

    private ViewPager viewpager;
    private Button guanzhu;
    private Button remen;
    private Button zuixin;
    private List<Fragment> listfra;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.shouye_item,null);
        viewpager =(ViewPager) view.findViewById(R.id.shouyepager);
        guanzhu =(Button) view.findViewById(R.id.guanzhu);
        remen =(Button) view.findViewById(R.id.remen);
        zuixin =(Button) view.findViewById(R.id.zuixin);
        init();

        return view;
    }

    private void init() {
        listfra = new ArrayList<Fragment>();

        listfra.add(new GuanZhuFragMent());
        listfra.add(new ReMenFragMent());
        listfra.add(new ZuiXinFragMent());

        //添加适配器
        viewpager.setAdapter(new MyAdapter(getChildFragmentManager()));

        //viewopager滑动监听
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        guanzhu.setTextColor(Color.parseColor("#D83A51"));
                        guanzhu.setTextSize(30);
                        remen.setTextColor(Color.BLACK);
                        remen.setTextSize(20);
                        zuixin.setTextColor(Color.BLACK);
                        zuixin.setTextSize(20);
                        break;

                    case 1:
                        remen.setTextColor(Color.parseColor("#D83A51"));
                        remen.setTextSize(30);
                        guanzhu.setTextColor(Color.BLACK);
                        guanzhu.setTextSize(20);
                        zuixin.setTextColor(Color.BLACK);
                        zuixin.setTextSize(20);
                        break;
                    case 2:
                        zuixin.setTextColor(Color.parseColor("#D83A51"));
                        zuixin.setTextSize(30);
                        remen.setTextColor(Color.BLACK);
                        remen.setTextSize(20);
                        guanzhu.setTextColor(Color.BLACK);
                        guanzhu.setTextSize(20);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
//适配器
    class MyAdapter extends FragmentPagerAdapter{


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listfra.get(position);
        }

        @Override
        public int getCount() {
            return listfra.size();
        }
    }

}
