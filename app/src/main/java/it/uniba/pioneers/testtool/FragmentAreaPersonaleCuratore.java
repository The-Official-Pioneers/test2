package it.uniba.pioneers.testtool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAreaPersonaleCuratore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAreaPersonaleCuratore extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAreaPersonaleCuratore() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAreaPersonaleCuratore.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAreaPersonaleCuratore newInstance(String param1, String param2) {
        FragmentAreaPersonaleCuratore fragment = new FragmentAreaPersonaleCuratore();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_area_personale_curatore, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDataCuratore();
    }

    @Override
    public void onResume() {
        super.onResume();
        setDataCuratore();
    }

    private void setDataCuratore(){

        ImageView propic = (ImageView) getActivity().findViewById(R.id.img_propic);
        EditText nome = (EditText) getActivity().findViewById(R.id.txt_nome);
        EditText cognome = (EditText) getActivity().findViewById(R.id.txt_cognome);
        EditText datanascita = (EditText) getActivity().findViewById(R.id.txt_datan);
        EditText email = (EditText) getActivity().findViewById(R.id.txt_email);
        EditText zona = (EditText) getActivity().findViewById(R.id.txt_zona);

        byte[] bytes = Base64.decode(MainActivity.curatore.getPropic(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        propic.setImageBitmap(decodedByte);
        propic.setScaleType(ImageView.ScaleType.CENTER_CROP);

        nome.setText(MainActivity.curatore.getNome());
        cognome.setText(MainActivity.curatore.getCognome());
        email.setText(MainActivity.curatore.getEmail());
        datanascita.setText(MainActivity.curatore.getShorterDataNascita());
        zona.setText( String.valueOf(MainActivity.curatore.getZona()) );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}