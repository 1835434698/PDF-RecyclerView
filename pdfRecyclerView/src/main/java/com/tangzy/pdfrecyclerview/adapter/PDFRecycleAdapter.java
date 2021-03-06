package com.tangzy.pdfrecyclerview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tangzy.pdfrecyclerview.bean.PdfDataBean;
import com.tangzy.pdfrecyclerview.view.ImageSource;
import com.tangzy.pdfrecyclerview.view.ScaleImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tangzy.pdfrecyclerview.R;


/**
 * class
 *
 * @author Administrator
 * @date 2019/12/23
 */
public class PDFRecycleAdapter extends RecyclerView.Adapter<PDFRecycleAdapter.Holder> {

    private Context mContext;
    protected String path;
    private List<PdfDataBean>  lists = new ArrayList<>();
    private int lastId = 0;


    /**
     * File descriptor of the PDF.
     */
    private ParcelFileDescriptor mFileDescriptor;

    /**
     * {@link android.graphics.pdf.PdfRenderer} to render the PDF.
     */
    private PdfRenderer mPdfRenderer;

    /**
     * Page that is currently shown on the screen.
     */
    private PdfRenderer.Page mCurrentPage;
    public PDFRecycleAdapter(Context context, String pdfPath) {
        this.path = pdfPath;
        this.mContext = context;
        init();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PDFRecycleAdapter.Holder holder = new PDFRecycleAdapter.Holder(LayoutInflater.from(mContext).inflate(R.layout.item_recycle_pdf, parent, false));
        return holder;
    }

    protected void init() {
        try {
            openRenderer(mContext);
            PdfDataBean bean;
            for (int i = getItemCount(); i>=0; i--){
                bean = new PdfDataBean();
                bean.pageId =i;
                lists.add(bean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up a {@link android.graphics.pdf.PdfRenderer} and related resources.
     */
    private void openRenderer(Context context) throws IOException {
        // In this sample, we read a PDF from the assets directory.

        File file = new File(path);
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        // This is the PdfRenderer we use to render the PDF.
        if (mFileDescriptor != null) {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        }
    }




    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Log.d("tagnzy", "onBindViewHolder position = "+position);
        Log.d("tagnzy", "getItemCount = "+getItemCount());
        Bitmap bitmap = null;
        if (lists.get(position).isShow){
            bitmap = getBigBitmap(position);
        }else {
            bitmap = getSmallBitmap(position);
        }
        if (bitmap != null){
            holder.image.setImage(ImageSource.bitmap(bitmap));
        }
    }

    private Bitmap getSmallBitmap(int index) {
        return getBitmap(index, 1f);
    }


    private Bitmap getBigBitmap(int index) {
        Log.d("tagnzy", "getBigBitmap index = "+index);

        return getBitmap(index, 2f);
    }

    private Bitmap getBitmap(int index, float DEFAULT_QUALITY) {
        Log.d("tagnzy", "DEFAULT_QUALITY = "+DEFAULT_QUALITY);

        if (mPdfRenderer.getPageCount() <= index) {
            return null;
        }
        // Make sure to close the current page before opening another one.
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        // Use `openPage` to open a specific page in PDF.
        mCurrentPage = mPdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap((int) (mCurrentPage.getWidth()*DEFAULT_QUALITY), (int)(mCurrentPage.getHeight()*DEFAULT_QUALITY),
                Bitmap.Config.ARGB_8888);
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // We are ready to show the Bitmap to user.
        return bitmap;
    }

    @Override
    public int getItemCount() {

        return mPdfRenderer != null ? mPdfRenderer.getPageCount() : 0;
    }

    public void setBigImage(int position) {
        lists.get(lastId).isShow = false;
        lists.get(position).isShow = true;
        lastId = position;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        private ScaleImageView image;

        public Holder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_pdf);
        }
    }
}
