/**
 * Where I'm at:
 *
 * Need to fix counter/spinner changer (doesn't change right number of times or work in general)
 *
 * 1. Dual-classroomed teachers in arrays twice
 * 2. McBrien crashes application
 * 3. Changing teacher changes classroom changes teacher to that classroom's default
 */

package com.example.newmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner teacherSpinner;
    private Spinner classroomSpinner;
    private TouchImageView map;
    private final int WIDTH = 3840;
    private final int HEIGHT = 2160;
    private Teacher[] teacherObjectArray;
    private Classroom[] classroomObjectArray;

    //Make this less sloppy!
    private int counter = 0;

    TeacherClassroomMap teacherClassroomMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Set up the map
        teacherClassroomMap = new TeacherClassroomMap(
                getResources().getStringArray(R.array.teachers_array),
                getResources().getStringArray(R.array.room_array_by_teacher),
                getResources().getStringArray(R.array.room_array),
                getResources().getIntArray(R.array.x_coordinates),
                getResources().getIntArray(R.array.y_coordinates));

        //Set up the spinners!
        teacherSpinner = (Spinner) findViewById(R.id.teacherSpinner);
        ArrayAdapter<String> teacherAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, teacherClassroomMap.getTeacherStrings());

        teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherSpinner.setAdapter(teacherAdapter);
        teacherSpinner.setOnItemSelectedListener(this);

        classroomSpinner = (Spinner) findViewById(R.id.classroomSpinner);
        ArrayAdapter<String> classroomAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.room_array));

        classroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classroomSpinner.setAdapter(classroomAdapter);
        classroomSpinner.setOnItemSelectedListener(this);



        teacherClassroomMap.printStrings();

        //Set up the arrays we'll need (we copy the ones from teacherClassroomMap because otherwise it's a bitch)
        teacherObjectArray = teacherClassroomMap.getTeacherObjectArray();

        classroomObjectArray = teacherClassroomMap.getClassroomObjectArray();
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {
        //Count each time we're told that a spinner has been changed. If it's been changed twice,
        //then that second time was us, ignore and reset.
        if(true) {
            counter++;

            if (parent == teacherSpinner) {
                Teacher tempTeacher = Resources.getTeacherByName((String) teacherSpinner.getSelectedItem(), teacherObjectArray);
                Classroom[] classrooms = teacherClassroomMap.getClassrooms(tempTeacher);
                int index = Resources.getIndexOfClassroom(classrooms[0], getResources().getStringArray(R.array.room_array));

                classroomSpinner.setSelection(index);
            } else if (parent == classroomSpinner) {
                Classroom tempClassroom = classroomObjectArray[classroomSpinner.getSelectedItemPosition()];
                Teacher tempTeacher = Resources.getTeacherFromClassroom(tempClassroom, teacherClassroomMap);
                Log.d("strings", "Found teacher of " + tempTeacher.getName());
                int index = Resources.getIndexOfTeacher(tempTeacher, teacherClassroomMap.getTeacherStrings());

                teacherSpinner.setSelection(index);
            }

            //Log.d("strings", "Times run:  " + Integer.toString(counter));
    }
        else
    {
            Log.d("strings", "Reset counter");
            counter = 0;
        }
    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}
