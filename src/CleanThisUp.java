import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import kareltherobot.Directions;
import kareltherobot.Robot;
import kareltherobot.World;

public class CleanThisUp implements Directions{

	int totalb = 0;
	int largeb = 0;
	double pileb = 0;
	int lave = 0;
	int lstr = 0;
	int b = 0;
	int l = 0;
	int w = 0;

	List<Integer> beeper = new ArrayList();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new CleanThisUp().start();
	}
	private void start() {
		setUpRoom();
		cleanRoom();
		reportStats();
	}

	private void setUpRoom(){
		JOptionPane.showMessageDialog(null, "Choose a world to load", "File Selection", JOptionPane.INFORMATION_MESSAGE);
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);
		File f = fc.getSelectedFile();
		World.readWorld(f.getAbsolutePath());
		World.setVisible(true);
		String delay = JOptionPane.showInputDialog(null, "Enter the speed of the robot" ,"Room Cleaner", JOptionPane.QUESTION_MESSAGE);
		World.setDelay(Integer.parseInt(delay));

	}

	private void cleanRoom() {
		String ave = JOptionPane.showInputDialog(null, "Enter the avenue(horizontal) where the robot starts" ,"Room Cleaner", JOptionPane.QUESTION_MESSAGE);
		String str = JOptionPane.showInputDialog(null, "Enter the street(vertical) where the robot starts" ,"Room Cleaner", JOptionPane.QUESTION_MESSAGE);
		String dir = JOptionPane.showInputDialog(null, "Enter the direction the robot faces" ,"Room Cleaner", JOptionPane.QUESTION_MESSAGE);
		dir.toLowerCase();
		Direction d = Direction.select(0);
		if(dir.equals("north")){
			d = Direction.select(1);
		}
		if(dir.equals("east")){
			d = Direction.select(2);
		}
		if(dir.equals("south")){
			d = Direction.select(3);
		}
		if(dir.equals("west")){
			d = Direction.select(4);
		}
		Robot r = new Robot(Integer.parseInt(str),Integer.parseInt(ave),d,0);

		corner(r);
		roomDimensions(r);
		corner(r);
		cleanUp(r);

		return;
	}

	private void cleanUp(Robot r) {
		// TODO Auto-generated method stub
		while(r.nextToABeeper()){
			r.pickBeeper();
			totalb++;
			b++;
			if(b == 1){
				pileb++;
			}
			if(b > largeb){
				largeb = b;
				lave = r.avenue();
				lstr = r.street();
			}
		}
		beeper.add(b);
		b = 0;
		while(r.frontIsClear()){
			r.move();
			while(r.nextToABeeper()){
				r.pickBeeper();
				totalb++;
				b++;
				if(b == 1){
					pileb++;
				}
				if(b > largeb){
					largeb = b;
					lave = r.avenue();
					lstr = r.street();
				}
			}
			beeper.add(b);
			b = 0;
			if(r.facingEast()){
				if(!r.frontIsClear()){
					r.turnLeft();
					if(!r.frontIsClear()){
						r.turnOff();
					}
					r.move();
					while(r.nextToABeeper()){
						r.pickBeeper();
						totalb++;
						b++;
						if(b == 1){
							pileb++;
						}
						if(b > largeb){
							largeb = b;
							lave = r.avenue();
							lstr = r.street();
						}
					}
					beeper.add(b);
					b = 0;
					r.turnLeft();
				}
			}
			if(r.facingWest()){
				if(!r.frontIsClear()){
					r.turnLeft();
					r.turnLeft();
					r.turnLeft();
					if(!r.frontIsClear()){
						r.turnOff();
					}
					r.move();
					while(r.nextToABeeper()){
						r.pickBeeper();
						totalb++;
						b++;
						if(b == 1){
							pileb++;
						}
						if(b > largeb){
							largeb = b;
							lave = r.avenue();
							lstr = r.street();
						}
					}
					beeper.add(b);
					b = 0;
					r.turnLeft();
					r.turnLeft();
					r.turnLeft();
				}
			}
		}
	}
	private void roomDimensions(Robot r) {
		// TODO Auto-generated method stub
		w++;
		while(r.frontIsClear()){
			r.move();
			w++;
		}
		if(!r.frontIsClear()){
			r.turnLeft();
		}
		l++;
		while(r.frontIsClear()){
			r.move();
			l++;
		}
	}
	private void corner(Robot r) {
		// TODO Auto-generated method stub
		while(!r.facingWest()){
			r.turnLeft();
		}
		while(r.frontIsClear()){
			r.move();
		}
		while(!r.facingSouth()){
			r.turnLeft();
		}
		while(r.frontIsClear()){
			r.move();
		}
		while(!r.facingEast()){
			r.turnLeft();
		}
	}
	private void reportStats() {
		// Prints out total beeps, highest pile, average pile size, 
		// percent clean (number of clean locations divided by area of room
		DecimalFormat df = new DecimalFormat("#.###");
		double area = l*w;

		
		System.out.println("");
		System.out.println("The area of the room is " +area + " units squared");
		System.out.println("There were " +totalb + " amount of beepers in the room");
		System.out.println("The largest pile of beepers were " +largeb + " beepers");
		System.out.println("The location of the largest beeper is at (" +lave+ "," +lstr+ ")");
		System.out.println("The amount of beeper piles were " +pileb);
		System.out.println("The percent of the room's dirtiness is " + df.format(pileb/area *100)+ "%");

		int sum = beeper.get(0);
		for(int i = 1; i < beeper.size(); i++){
			sum += beeper.get(i);
		}
		System.out.println("The average pile size " +df.format(sum/pileb));

	}

}
