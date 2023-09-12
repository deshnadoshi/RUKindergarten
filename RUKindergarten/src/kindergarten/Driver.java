package kindergarten;
import java.util.*;
import java.io.File;
/**
 * This class is designed to test each method in the Transit file interactively
 * 
 * @author Ethan Chou 
 * @author Maksims Kurjanovics Kravcenko
 * @author Kal Pandit
 */

public class Driver {
    public static void main ( String[] args ) {
		String[] methods = {"makeClassroom", "setupSeats", "seatStudents", "insertMusicalChairs", "playMusicalChairs", "addLateStudent", "deleteLeavingStudent"};
		String[] options = {"Test a new input file", "Test another method on the same file", "Quit"};
		int controlChoice = 1;
        Classroom studentClassroom = new Classroom();
		
		do {
			StdOut.print("Enter a student info input file => ");
			String inputFile = StdIn.readLine();

			do {
				StdOut.println("\nWhat method would you like to test?");
				for (int i = 0; i < methods.length; i++) {
					StdOut.printf("%d. %s\n", i+1, methods[i]);
				}
				StdOut.print("Enter a number => ");
				int choice = Integer.parseInt(StdIn.readLine());

				switch (choice) {
					case 1:
						studentClassroom = testMakeClassroom(inputFile);
						studentClassroom.printClassroom();
						break;
					case 2:
						testSetupSeats(studentClassroom);
						studentClassroom.printClassroom();
						break;
                    case 3:
                        testSeatStudents(studentClassroom);
						studentClassroom.printClassroom();
                        break;
					case 4:
						testInsertMusicalChairs(studentClassroom);
						studentClassroom.printClassroom();
						break;
					case 5:
						testPlayMusicalChairs(studentClassroom);
						studentClassroom.printClassroom();
						break;
					case 6:
						testAddStudent(studentClassroom);
						studentClassroom.printClassroom();
						break;
					case 7:
						testDeleteStudent(studentClassroom);
						studentClassroom.printClassroom();
						break;
					default:
						StdOut.println("Not a valid option!");
				}
				StdIn.resetFile();
				StdOut.println("What would you like to do now?");
				for ( int i = 0; i < 3; i++ ) {
					StdOut.printf("%d. %s\n", i+1, options[i]);
				}
				StdOut.print("Enter a number => ");
				controlChoice = Integer.parseInt ( StdIn.readLine() );
			} while ( controlChoice == 2 );
		} while ( controlChoice == 1 );
    }

    private static Classroom testMakeClassroom (String filename) {
		// Call student's makeList method
	    Classroom studentClassroom = new Classroom();
		studentClassroom.makeClassroom(filename);
		return studentClassroom;
    }
	private static void testSetupSeats (Classroom studentClassroom) {
		StdOut.print("Enter a seating availability input file => ");
		String inputFile = StdIn.readLine();
		studentClassroom.setupSeats(inputFile);
	} 

	private static void testSeatStudents (Classroom studentClassroom) {
		studentClassroom.seatStudents();
	}

	private static void testInsertMusicalChairs (Classroom studentClassroom) {
		studentClassroom.insertMusicalChairs();
	}
	
	private static void testPlayMusicalChairs (Classroom studentClassroom) {
	    StdRandom.setSeed(2022);
		StdOut.println("Here is the classroom after a long game of musical chairs: ");
		studentClassroom.playMusicalChairs();
		StdOut.println();
	}

	private static void testAddStudent (Classroom studentClassroom) {
        StdOut.print("\nWrite the student's first name -> ");
        String studentName = StdIn.readString();
		StdOut.print("\nWrite the student's last name -> ");
        String lastName = StdIn.readString();
		StdOut.print("\nWrite the student's height as a number -> ");
        int height = StdIn.readInt();
        studentClassroom.addLateStudent(studentName, lastName, height);
	}
	
	private static void testDeleteStudent (Classroom studentClassroom) {
        StdOut.print("\nWrite the student's first name -> ");
        String firstName = StdIn.readString();
		StdOut.print("\nWrite the student's last name -> ");
        String lastName = StdIn.readString();
        studentClassroom.deleteLeavingStudent(firstName, lastName);
	}
}
