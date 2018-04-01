package com.example.alexchar.studyorganizer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SubjectInputFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    RadioButton radioButtonOb;
    RadioButton radioButtonCh;
    SeekBar seekBar;
    SubjectDatabase sDatabase;

    private OnFragmentInteractionListener mListener;

    public SubjectInputFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubjectInputFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectInputFragment newInstance(String param1, String param2) {
        SubjectInputFragment fragment = new SubjectInputFragment();
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
        final View v = inflater.inflate(R.layout.fragment_subject_input, container, false);
        seekBar = (SeekBar) v.findViewById(R.id.semester_seekbar);
        TextView textView = (TextView) v.findViewById(R.id.semester_bar);
        radioButtonOb = (RadioButton) v.findViewById(R.id.radio_obligatory);
        radioButtonCh = (RadioButton) v.findViewById(R.id.radio_choose);
        Button button_cancel = (Button) v.findViewById(R.id.cancel_button);
        Button button_submit = (Button) v.findViewById(R.id.submit_button);
        sDatabase = SubjectDatabase.getSubjectDatabase(getActivity());

        //Setting cancel and submit buttons actions
        cancelSubject(button_cancel);
        submitSubject(button_submit);
        //Calling method to set TextView value of the seekBar Input
        setSemesterSeekBar(seekBar,textView);
        radioButtonOb.setChecked(true);
        return v;
    }

    private void submitSubject(Button button_submit) {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Start the activity again so the recylcer view will get updated.
                TextView name =  getActivity().findViewById(R.id.fSubject_name);

                if(name.getText().toString().trim().length() > 0 ){
                    //Add subject to database.
                    addSubjectToDatabase();
                    Intent intent = new Intent(getActivity(), SubjectActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Προστέθηκε καινούριο μάθημα!", Toast.LENGTH_LONG).show();
                }else{
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getContext());
                    }
                    builder.setTitle("Προσθέστε Μάθημα")
                            .setMessage("Το πεδίο με το όνομα του μαθήματος δεν μπορεί να είναι κενό.")
                            .setNeutralButton("Οκ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }




            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setSemesterSeekBar(SeekBar seekBar,final TextView textView){

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                textView.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void cancelSubject(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectActivity subjectActivity = (SubjectActivity) getActivity();
                subjectActivity.onBackPressed();

            }
        });
    }

    public void addSubjectToDatabase(){
        //get data from user and store them in subject variable
        Subject subject = new Subject();
        TextView name =  getActivity().findViewById(R.id.fSubject_name);
        subject.setSubjectName( name.getText().toString());
        if(radioButtonCh.isChecked()){
            TextView radio =  getActivity().findViewById(R.id.radio_choose);
            subject.setSubjectType( radio.getText().toString());
        }else if(radioButtonOb.isChecked()){
            TextView radio =  getActivity().findViewById(R.id.radio_obligatory);
            subject.setSubjectType( radio.getText().toString());
        }
        subject.setSubjectSemester( Integer.toString(seekBar.getProgress() + 1 ));
        TextView teacher =  getActivity().findViewById(R.id.fSubject_teacher);
        subject.setSubjectTeacher( teacher.getText().toString());
        TextView hours =  getActivity().findViewById(R.id.fSubject_hours);
        subject.setSubjectHours(hours.getText().toString());
        TextView room = getActivity().findViewById(R.id.fSubject_room);
        subject.setSubjectRoom( room.getText().toString());
        TextView points = getActivity().findViewById(R.id.fSubject_points);
        subject.setSubjectPoints( points.getText().toString());
        //Hard code - set grade to 0
        subject.setSubjectGrade(0);
        //add the subject to Database
        sDatabase.subjectDao().insertAll(subject);
    }
}
