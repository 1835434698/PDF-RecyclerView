package com.tangzy.pdfrecyclerview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tangzy.pdfrecyclerview.adapter.LinearLayoutManagerRecycleview;
import com.tangzy.pdfrecyclerview.adapter.OnScrollListenerRecycleView;
import com.tangzy.pdfrecyclerview.adapter.PDFRecycleAdapter;

public class RecycleViewFragment extends Fragment {
    private PDFRecycleAdapter adapter;
    private RecyclerView recycleView;
    private TextView tvNum;
    private static final String PATH = "path";//fragment的序号

    /**
     * 创建新实例
     *
     * @param url
     * @return
     */
    public static RecycleViewFragment newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(PATH, url);
        RecycleViewFragment fragment = new RecycleViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.fragment_recycleview, null);
        init(inflate);
        return inflate;
    }



    private void init(View view) {
        String url = getArguments().getString(PATH);
        recycleView = view.findViewById(R.id.recycleView);
        tvNum = view.findViewById(R.id.tvNum);
        adapter = new PDFRecycleAdapter(getActivity(), url);
        //1线性
        LinearLayoutManagerRecycleview layoutManager = new LinearLayoutManagerRecycleview(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//

        recycleView.setLayoutManager(layoutManager);

        recycleView.setAdapter(adapter);
        recycleView.addOnScrollListener(new OnScrollListenerRecycleView(){
            @Override
            public void onAdapterChanged(int position) {
                super.onAdapterChanged(position);
                setTextCount(position);
            }
        });
        setTextCount(0);

    }

    private void setTextCount(int lastVisibleItemPosition) {
        Log.d("tagnzy", "lastVisibleItemPosition========================= "+(lastVisibleItemPosition+1)+"/"+adapter.getItemCount());
        tvNum.setText((lastVisibleItemPosition+1)+"/"+adapter.getItemCount());
        adapter.setBigImage(lastVisibleItemPosition);
    }

}
