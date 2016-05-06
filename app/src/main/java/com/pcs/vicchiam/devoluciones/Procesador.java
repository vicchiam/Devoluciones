package com.pcs.vicchiam.devoluciones;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicch on 04/05/2016.
 */
public class Procesador {

    public Procesador(){

    }

    public Bitmap procesa(Bitmap img){
        Mat entrada=new Mat();
        Utils.bitmapToMat(img, entrada);

        Mat salida=new Mat();
        Imgproc.cvtColor(entrada,salida,Imgproc.COLOR_RGBA2GRAY);
        Imgproc.GaussianBlur(salida,salida,new Size(5,5),0);
        Imgproc.Canny(salida,salida,50,200);



        List<Rect> rects=new ArrayList<Rect>();

        //Buscar Contornos
        List<MatOfPoint> contornos=new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(salida.clone(),contornos,hierarchy,Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
        for(int i=0;i<contornos.size();i++){
            Rect rect = Imgproc.boundingRect(contornos.get(i));
            if(rect.width<200 || rect.height<200){
                continue;
            }
            else{
                //validos.add(contornos.get(i));
                rects.add(rect);
                break;
            }
        }
        hierarchy.release();
        salida.release();

        if(rects.size()>0) {
            /*Imgproc.rectangle(
                entrada,
                new Point(rect.x,rect.y),
                new Point(rect.x+rect.width,rect.y+rect.height),
                new Scalar(0,255,0)
            );*/

            Rect rect=null;
            for(int i=0;i<rects.size();i++){
                if(rect==null){
                    rect=rects.get(i);
                }
                if(rect.width<rects.get(i).width || rect.height<rects.get(i).height){
                    rect=rects.get(i);
                }
            }

            Mat aux=new Mat(entrada.clone(),rect);
            Imgproc.resize( aux, entrada, new Size(entrada.width(),entrada.height()));
            aux.release();
        }

        Utils.matToBitmap(entrada,img);
        entrada.release();

        return img;
    }

}
