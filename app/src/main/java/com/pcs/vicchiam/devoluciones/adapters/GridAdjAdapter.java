package com.pcs.vicchiam.devoluciones.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pcs.vicchiam.devoluciones.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 02/09/2016.
 */
public class GridAdjAdapter extends BaseAdapter {

    private Context context;
    private List<String> imgs;

    public GridAdjAdapter(Context context){
        this.context=context;
        imgs=new ArrayList<>();
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public Object getItem(int position) {
        return imgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item_adj, parent, false);
        }

        File imgFile = new File(imgs.get(position));
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) view.findViewById(R.id.imgAdj);
            myImage.setImageBitmap(myBitmap);
        }

        return view;
    }

    public void anyadirImagen(String imagen){
        imgs.add(imagen);
        notifyDataSetInvalidated();
    }

    public void eliminarImagen(int pos){
        imgs.remove(pos);
    }
}
