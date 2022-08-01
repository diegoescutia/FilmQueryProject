package com.skilldistillery.filmquery.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor dao = new DatabaseAccessorObject();
	Film film = new Film();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
		// app.test();
		app.launch();
	}

	// private void test() {
	// Film film = db.findFilmById(1);
	// System.out.println(film);
	// }

	private void launch() {
		Scanner scan = new Scanner(System.in);

		startUserInterface(scan);

		scan.close();
	}

	private void startUserInterface(Scanner scan) {

		boolean trigger = true;
		while (trigger) {
			menu();
			int selection = scan.nextInt();
			scan.nextLine();

			switch (selection) {

			case 1:
				filmById(scan);

				break;
			case 2:
				keyword(scan);
				break;
			case 3:
				System.out.println("Thank you for using our app");
				trigger = false;
				break;
			default:
				System.out.println("Enter a valid option number");
			}
		}
	}

	public void filmById(Scanner scan) {

		System.out.println("Enter film ID:");
		int selection = scan.nextInt();
		film = dao.findFilmById(selection);
		if (film == null) {
			System.out.println("Could not find films with id:" + selection);
		} else {

			String lang = dao.findLanguageByFilm(film.getLanguageId());
			System.out.println(film.toString());
			System.out.println("LANGUAGE: " + lang);
			System.out.println("CAST:");
			System.out.println(dao.findActorsByFilmId(film.getId()));

		}

	}

	public void keyword(Scanner scan) {
		DatabaseAccessorObject daos = new DatabaseAccessorObject();
		List<Film> listByKeyword = new ArrayList<>();
		System.out.println("Enter the name or keyword of the film:");
		String keyword = scan.nextLine();
		listByKeyword = dao.findFilmByKeyWord(keyword);

		for (Film film : listByKeyword) {
			System.out.println(film);
			System.out.println(daos.findLanguageByFilm(film.getLanguageId()));
			System.out.println(daos.findActorsByFilmId(film.getId()));
		}
		System.out.println("*****Found: " + listByKeyword.size() + " matches*****");
	}

	public void menu() {
		System.out.println("         =================");
		System.out.println("         Welcome FilmMania");
		System.out.println("         =================");
		System.out.println("-----------------------------------");
		System.out.println("|  Type the number for menu option|");
		System.out.println("|        1) Find film by ID       |");
		System.out.println("|        2) Find by keyword       |");
		System.out.println("|        3) Exit                  |");
		System.out.println("-----------------------------------");
	}
}
