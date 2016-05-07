package com.pcs.vicchiam.devoluciones.utilidades;

/**
 * Created by vicch on 05/05/2016.
 */
public class Obsoleto {

    /*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HACER_FOTO && resultCode == RESULT_OK) {
            if (data != null) {
                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection, null, null, null);
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();
                String imagePath = cursor.getString(column_index_data);
                procesarEscaner(imagePath);
            }
        }
        else{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() != null) {
                    String codigoBarras=result.getContents();
                    procesarCodigoBarras(codigoBarras);
                }
            }
        }

    }

    private void leerCodigo(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        integrator.initiateScan();
    }

    private void escaner(){
        Intent hacerFoto=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hacerFoto.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(hacerFoto, HACER_FOTO);
        }
    }

    private void procesarCodigoBarras(String codigoBarras){
        String p3=codigoBarras.substring(0,3);
        String empresa=codigoBarras.substring(3,10);
        String codigo=codigoBarras.substring(10,15);
        String p3_2=codigoBarras.substring(15,18);
        String fecha=codigoBarras.substring(18,24);
        String p3_3=codigoBarras.substring(24,26);
        String lote=codigoBarras.substring(26);
        if(!empresa.equals("8411530")){
            Utilidades.Alerts(this,"Atención","Este codigo no pertenece a la empresa",0);
        }
        if(!Utilidades.Wifi(this)){
            Utilidades.Alerts(this,"Atención","No esta conectado a la red WIFI",0);
        }
        else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("operacion", "obtener_descripcion");
            hashMap.put("codigo", codigo);
            Conexion comunicacion = new Conexion(this);
            comunicacion.execute(hashMap);
        }
    }

    public void respuesta(String opcion, String respuesta){
        Log.e("MAIN",opcion+" "+respuesta);
    }

    private void procesarEscaner(String imagePath){
        try {
            Bitmap image=BitmapFactory.decodeFile(imagePath);

            File fImage=new File(imagePath);
            if(fImage.exists()){
                fImage.delete();
            }
            FileOutputStream fOut=new FileOutputStream(fImage);
            image.compress(Bitmap.CompressFormat.PNG,100,fOut);
            Log.e("AAAA",fImage.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("EEEEEE","EEEEEEEEEEE");
        }

    }
     */

}
