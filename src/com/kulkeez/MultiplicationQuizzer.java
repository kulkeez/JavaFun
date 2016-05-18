package com.kulkeez;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 
 * A quiz program to reinforce multiplication tables. I got bored 
 * asking random multiplication questions and delegated it to this program.
 * 
 * @author Vikram Kulkarni
 *
 */
public class MultiplicationQuizzer {

	static List<Integer> integerList = new LinkedList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			BufferedReader stdin = 
	                      new BufferedReader(new InputStreamReader(System.in));
			
			Collections.shuffle(integerList);
			
			//TODO: see if Apache Commons CLI can be used
			String input, userAnswer;
			int answer = 0, randInteger = 0;
			int score = 0, totalQuestions = 0;
			
			System.out.println("Enter your name: ");	
			String userName = stdin.readLine();
			
			System.out.println("Hello " + userName + "! Testing how well you have memorized multiplication tables. All the best!");	
			System.out.println("Practice which table? Enter any number (between 1 - 10):");
			
			if((input = stdin.readLine()) != null){
				
				// Pose only 10 questions - needs to be configurable
				while (++totalQuestions <= 10) {
					//commenting out this call as randomness generating same integers again
					//randInteger = getRandomNumberInRange(1, 10);
					randInteger = getRandomNumberFromShuffleList();
				
					//System.out.println("randInteger = " + randInteger);
					//System.out.println("input = " + input);

					System.out.println(totalQuestions + ". What is " + input  + " X " + randInteger + ": ");
					answer = Integer.parseInt(input) * randInteger;

					userAnswer = stdin.readLine();
					
					if(userAnswer != null && !"".equals(userAnswer)){
						System.out.println("Your answer = " + Integer.parseInt(userAnswer));

						if(Integer.parseInt(userAnswer) == answer) {
							score++;
							System.out.println("Good! Your Score = " + score + "/" + totalQuestions);
						}
						else {
							System.out.println("No " + userName + "! The correct Answer: " 
									+ input  + " X " + randInteger + " = " + answer +
									". Your Score = " + score + "/" + totalQuestions);
							System.out.println();
						}
					}
					else {
						System.out.println("Whoa! You should input an integer and then hit <Enter>.");
						--totalQuestions;
						integerList.add(randInteger);
					}
				}
			}
			
			// Print final result
			if(score <= 7)
				System.out.println("Hey " + userName + ", practice, practice, practice !");
			else if(score < 9 ) 
				System.out.println("Good job, " + userName + "!!!");
			else  
				System.out.println("Awesome, " + userName + "!!!");
		}
		catch(IOException io){
			io.printStackTrace();
		}	
	}

	/**
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	private static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	
	/**
	 * 
	 * Return a integer between 1 - 10
	 * 
	 * @return
	 */
	private static int getRandomNumberFromShuffleList() {
		long seed = System.nanoTime();
		Integer randInteger = null;
		if(!integerList.isEmpty()) {
			randInteger = integerList.get(0);
			integerList.remove(0);
			//System.out.println("List now has:  " + integerList);
		}
		return randInteger;
	}
	
}
