package kindergarten;
/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {

        StdIn.setFile(filename);

        int num_students = StdIn.readInt(); 

        Student [] ordered_student_list = new Student[num_students]; 

        // Reading from file and storing Students into an array
        for (int i = 0; i < num_students; i++){
            String first_name = StdIn.readString(); 
            String last_name = StdIn.readString();
            int height = StdIn.readInt(); 

            Student student = new Student (first_name, last_name, height); 
            ordered_student_list[i] = student; 
        }

        // Storing Students in alphabetical order in array
        for (int i = 0; i < num_students; i++){
            for (int j = i + 1; j < num_students; j++){

                int comparison_val = ordered_student_list[i].compareNameTo(ordered_student_list[j]); 
                if (comparison_val < 0){
                    Student temp = ordered_student_list[i]; 
                    ordered_student_list[i] = ordered_student_list[j];
                    ordered_student_list[j] = temp; 
                }
                

            }
        }

        // Adds students to linked list from alphabetically ordered array
        for (int i = 0; i < num_students; i++){
            studentsInLine = new SNode(ordered_student_list[i], studentsInLine);  
        }

        
    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *  
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {

        StdIn.setFile(seatingChart);
        int row = StdIn.readInt(); 
        int col = StdIn.readInt(); 
        
        seatingAvailability = new boolean [row][col]; 
        studentsSitting = new Student[row][col]; 

        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                if (StdIn.readBoolean() == true){
                    seatingAvailability[i][j] = true; 
                } else {
                    seatingAvailability[i][j] = false; 
                }
            }
        }
	    
    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents () {
        SNode ptr = studentsInLine;   
        int musical_seat_count = 0; 

        if (musicalChairs != null){
            for (int i = 0; i < seatingAvailability.length; i++){
                for (int j = 0; j < seatingAvailability[i].length; j++){
                    if (seatingAvailability[i][j] == true && musical_seat_count == 0){
                        studentsSitting[i][j] = musicalChairs.getNext().getStudent(); 
                        seatingAvailability[i][j] = false; 
                        musical_seat_count++; 
                        break; 
                    }
                }
            }
        }

        
        for (int i = 0; i < seatingAvailability.length; i++){
            for (int j = 0; j < seatingAvailability[i].length; j++){
                if (seatingAvailability[i][j] == true){
                    if (ptr != null){
                        studentsSitting[i][j] = ptr.getStudent();
                        ptr = ptr.getNext();  
                    }
                }
            }
        }
	
        studentsInLine = null; 

    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */

    public void insertMusicalChairs () {
        
        for (int i = 0; i < studentsSitting.length; i++){
            for (int j = 0; j < studentsSitting[i].length; j++){
                
                SNode addStudent = new SNode();
                
                if (studentsSitting[i][j] != null){
                    addStudent.setStudent(studentsSitting[i][j]); 
                    // System.out.println(addStudent.getStudent().getFullName());
                    
                    if (musicalChairs == null){
                        musicalChairs = addStudent; 
                        addStudent.setNext(musicalChairs);
                        musicalChairs.setNext(addStudent);
                        addStudent.setNext(musicalChairs);
                        
    
                    } else {
                        SNode temp = new SNode(); 
                        temp = musicalChairs; 
                        while (temp.getNext() != musicalChairs){
                            temp = temp.getNext(); 
                        }
    
                        temp.setNext(addStudent);
                        addStudent.setNext(musicalChairs);
                    }

                    studentsSitting[i][j] = null; 
                } else {
                    continue; 
                }
                
                

            }
        }

        SNode ptr = new SNode(); 
        ptr = musicalChairs; 
        int count = 0; 

        do {
            ptr = ptr.getNext();
            count++;  
        } while (ptr != musicalChairs); 


        ptr = musicalChairs; 

        for (int i = 0; i < count - 1; i++){
            ptr = ptr.getNext(); 
        }

        SNode temp = new SNode(); 
        temp = musicalChairs; // holds m. brow

        musicalChairs = ptr; // sets last to f. wolf
        ptr.setNext(temp); // sets f.wolf next to m.brow 
        // System.out.println(temp.getNext().getStudent().getFullName()); 
        
     }

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the 
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in studentsInLine 
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    public void playMusicalChairs() {
         
        int count_deletes = 0; 
        int size = 2; 
        int max_deletes = 0; 
        ;  

        SNode ptr_count = musicalChairs; 
        
        do{
            ptr_count = ptr_count.getNext(); 
            max_deletes++; 
        } while (ptr_count != musicalChairs && ptr_count != null); 
        
        Student [] heightOrdered = new Student[max_deletes - 1]; 

        
        while (size > 1) {
            size = 0; 

            SNode ptr = musicalChairs; 
            // System.out.println("pointer: " + ptr.getStudent().getFullName()); 

            do {
                ptr = ptr.getNext(); 
                // System.out.println("pointer loop: " + ptr.getStudent().getFullName()); 
                size++; 
            } while (ptr != musicalChairs && ptr != null);

           //  System.out.println("size: " + size);

            int n = StdRandom.uniform(size);
            
            SNode delPrev = musicalChairs.getNext(); 
            SNode delTerm = musicalChairs.getNext(); 

            // System.out.println("starting point: " + delPrev.getStudent().getFullName()); 

           //  System.out.println("index: " + n);

            int n_prev = n - 1;

            if (n_prev < 0){
                n_prev = size - 1; 
            }

            for (int i = 0; i < n_prev; i++){
                delPrev = delPrev.getNext(); 
            }

            for (int i = 0; i < n; i++){
                delTerm = delTerm.getNext(); 
            }

            if (size == 1){
                // System.out.println("last student remaning"); 
                break; 
            }

            // System.out.println("deleted.prev: " + delPrev.getStudent().getFullName()); 
            // System.out.println("deleted student: " + delTerm.getStudent().getFullName()); 


            delPrev.setNext(delTerm.getNext());

            // to avoid the null pointer issue when the last node loses
            
            if (n == (size - 1)){
                musicalChairs = delPrev; 
            }
            
            // System.out.println("new link: " + delPrev.getNext().getStudent().getFullName());

            delTerm.setNext(null);



            heightOrdered[count_deletes] = delTerm.getStudent(); 
            // System.out.print("student deletion order:" + delTerm.getStudent().getFullName()); 

            count_deletes++;

            // need to order by height in the array and then add to studentsInLine 

        }

        
        Student [] order_deleted = new Student [heightOrdered.length]; 
        for (int i = 0; i < heightOrdered.length; i++){
            order_deleted[i] = heightOrdered[i]; 
        }
        
        for (int i = 0; i < max_deletes - 1; i++){
            for (int j = i + 1; j < max_deletes - 1; j++){
                if (heightOrdered[i].getHeight() > heightOrdered[j].getHeight()){
                    Student temp = heightOrdered[j]; 
                    heightOrdered[j] = heightOrdered[i]; 
                    heightOrdered[i] = temp; 
                } else if (heightOrdered[i].getHeight() == heightOrdered[j].getHeight()){
                    for (int k = 0; k < order_deleted.length; k++){
                        int index_i = 0; 
                        int index_j = 0; 

                        if (order_deleted[k] == heightOrdered[i]){
                            index_i = k; 
                        }

                        if (order_deleted[k] == heightOrdered[j]){
                            index_j = k; 
                        }

                        if (index_j < index_i){
                            Student temp = heightOrdered[j]; 
                            heightOrdered[j] = heightOrdered[i];
                            heightOrdered[i] = temp; 
                        }
                    }
                }
            }
        }
        

        

        for (int i = heightOrdered.length - 1; i >= 0; i--){
            studentsInLine = new SNode(heightOrdered[i], studentsInLine); 
            
        }

        seatStudents(); // need to add into this musical chairs being seated first
    } 

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    public void addLateStudent ( String firstName, String lastName, int height ) {
        
        Student lateStudent = new Student(firstName, lastName, height); 
        SNode add_student = new SNode(lateStudent, null); 

        SNode music_ptr = musicalChairs; 
        int music_size = 0; 

        boolean students_in_seats = false; 

        for (int i = 0; i < studentsSitting.length; i++){
            for (int j = 0; j < studentsSitting[i].length; j++){
                if (studentsSitting[i][j] != null){
                    students_in_seats = true; 
                    break; 
                }
            }
        }
        
        if (musicalChairs != null){
            do {
                music_ptr = music_ptr.getNext(); 
                music_size++; 
            } while (music_ptr != musicalChairs && music_ptr != null);
        }


        SNode line_ptr = studentsInLine; 
        // check if they're in line
        if (studentsInLine != null){
            while (line_ptr.getNext() != null){
                line_ptr = line_ptr.getNext(); 
            }
            line_ptr.setNext(add_student);
            add_student.setNext(null);
            // check in mc 
        } else if (music_size > 1){
            SNode insert_mc_ptr = musicalChairs; 
            for (int i = 0; i < music_size; i++){
                insert_mc_ptr = insert_mc_ptr.getNext(); 
            }

            System.out.println(insert_mc_ptr.getStudent().getFullName() + "last node"); 
            System.out.println(musicalChairs.getNext().getStudent().getFullName() + "next node"); 

            insert_mc_ptr.setNext(add_student);
            // add_student.setNext(musicalChairs.getNext());
            musicalChairs = add_student; 

            // add_student.setNext(insert_mc_ptr.getNext().getNext());
           
            // add_student.setNext(musicalChairs.getNext());
            // musicalChairs = add_student;    

            // FIX THIS 

        } else if (students_in_seats == true){

            int sat_down = 0; 

            for (int i = 0; i < seatingAvailability.length; i++){
                for (int j = 0; j < seatingAvailability[i].length; j++){
                    if (seatingAvailability[i][j] == true && studentsSitting[i][j] == null && sat_down == 0){
                        studentsSitting[i][j] = add_student.getStudent(); 
                        sat_down++; 
                        break; 
                    }
                }
            }
        }
        
    }

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students 
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName the student's last name
     */
    public void deleteLeavingStudent ( String firstName, String lastName ) {

        boolean found_person = false; 

        if (studentsSitting != null){
            for (int i = 0; i < studentsSitting.length; i++){
                for (int j = 0; j < studentsSitting[i].length; j++){
                    if (studentsSitting[i][j] != null){
                        if (studentsSitting[i][j].getFirstName().equals(firstName) && studentsSitting[i][j].getLastName().equals(lastName)){
                            studentsSitting[i][j] = null; 
                            seatingAvailability[i][j] = true; 
                            // found_person = true; 
                        }
                    }
                    
                }
            }
        }
        

        if (found_person == false ){
            SNode musical_prev_ptr = musicalChairs; 
            SNode musical_del_ptr = musicalChairs; 
    
            int count_musical_ind = 0; 
    
            if (musicalChairs != null){
    
                SNode ptr_size = musicalChairs; 
                int musical_chairs_size = 0;
                int musical_chairs_fixed_size = 0; 
                
                do {
                    ptr_size = ptr_size.getNext(); 
                    musical_chairs_size++; 
                } while (ptr_size != musicalChairs && ptr_size != null);
                musical_chairs_fixed_size = musical_chairs_size; 
    
                while (!musical_del_ptr.getStudent().getFirstName().equals(firstName) && !musical_del_ptr.getStudent().equals(lastName) && musical_chairs_size > 0){
                    musical_del_ptr = musical_del_ptr.getNext(); 
                    musical_chairs_size--; // might need to fix 
                    count_musical_ind++; 
                }

                if (musical_chairs_size == 0){
                    found_person = false; 
                } else if (musical_chairs_size == 1 && count_musical_ind == 0){
                    musicalChairs = null; 
                } else {
                    int prev_count_musical_ind = count_musical_ind - 1; 

                    // System.out.println(prev_count_musical_ind + " prev ind");
                    // System.out.println(count_musical_ind + " del ind");

                    if (prev_count_musical_ind < 0){
                        prev_count_musical_ind = musical_chairs_fixed_size + prev_count_musical_ind; 
                    }

                    // System.out.println(prev_count_musical_ind + " prev ind modify");

                
                    musical_del_ptr = musicalChairs; 
        
                    for (int i = 0; i < prev_count_musical_ind; i++){
                        musical_prev_ptr = musical_prev_ptr.getNext(); 
                    }
        
                    // System.out.println(musical_prev_ptr.getStudent().getFullName() + " PREVIOUS"); 
        
            
                    for (int i = 0; i < count_musical_ind; i++){
                        musical_del_ptr = musical_del_ptr.getNext(); 
                    }

                    // System.out.println(musical_del_ptr.getStudent().getFullName() + " DELETE"); 
                    System.out.println(musical_del_ptr.getNext().getStudent().getFullName() + " NEXT DELETE"); 

                    // System.out.println(musical_chairs_fixed_size + " fixed size"); 
                    // System.out.println(count_musical_ind + " index "); 

                    if (prev_count_musical_ind == (musical_chairs_fixed_size - 1)){
                        musical_prev_ptr.setNext(musical_del_ptr.getNext());
                        musicalChairs = musical_prev_ptr; 
                    } else {
                        musical_prev_ptr.setNext(musical_del_ptr.getNext());
                        musical_del_ptr.setNext(null);
                    }
        
                    // System.out.println(musicalChairs.getNext().getStudent().getFullName() + " next stu full"); 
                    found_person = true; 
                }
    
                
        } 

        if (found_person == false){
            // check in students in line 
            // System.out.println("line check"); 

            int count_line_del_ind = 0; 
            int count_line_prev_ind = 0; 

            if (studentsInLine != null){

                SNode line_ptr = studentsInLine; 
                while (line_ptr != null){
                    if (line_ptr.getStudent().getFirstName().equals(firstName) && line_ptr.getStudent().getLastName().equals(lastName)){
                        found_person = true; 
                        break; 
                    }

                    line_ptr = line_ptr.getNext(); 
                    count_line_del_ind++; 
                    
                }


                // check if prev becomes negative

                count_line_prev_ind = count_line_del_ind - 1; 

                SNode line_del_ptr = studentsInLine; 
                SNode line_prev_ptr = studentsInLine; 

                if (count_line_prev_ind < 0){

                    studentsInLine = line_del_ptr.getNext(); 
                    line_del_ptr.setNext(null); 

                } else {
                    for (int i = 0; i < count_line_prev_ind; i++){
                        line_prev_ptr = line_prev_ptr.getNext(); 
                    }
    
    
                    for (int i = 0; i < count_line_del_ind; i++){
                        line_del_ptr = line_del_ptr.getNext(); 
                    }

                    line_prev_ptr.setNext(line_del_ptr.getNext());
                    line_del_ptr.setNext(null); 
                }

                

                


            }

        }


        



            
        }



    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
