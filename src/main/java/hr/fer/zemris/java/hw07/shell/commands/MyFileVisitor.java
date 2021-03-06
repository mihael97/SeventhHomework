package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Razred koji implementira sučelje {@link SimpleFileVisitor} i služi za
 * nerekurzivni prolazaka kroz datoteke
 * 
 * @author Mihael
 *
 */
public class MyFileVisitor extends SimpleFileVisitor<Path> {
	/**
	 * Lista datoteka koje su do sada obrađene
	 */
	private List<String> list;

	/**
	 * Zadani konstruktor
	 */
	public MyFileVisitor() {
		list = new ArrayList<>();
	}

	/**
	 * Glavna metoda koja prolazi kroz sve datoteke i stavra opisnik datoteka koji
	 * sadrži određene informacije o datoteki(direktorij,čitljiva,da li se može
	 * pokrenuti ili modficirati,datum i vrijeme nastanka te naziv)
	 * 
	 * @return {@link FileVisitResult} za nastavak prolaska
	 */
	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes arg1) throws IOException {
		File file = path.toFile();
		StringBuilder builder = new StringBuilder();

		builder.append(file.isDirectory() ? 'd' : '-');
		builder.append(file.canRead() ? 'r' : '-');
		builder.append(file.canWrite() ? 'w' : '-');
		builder.append(file.canExecute() ? 'e' : '-');
		builder.append(" ");

		builder.append(String.format("%10d", file.length()));
		builder.append(" ");

		builder.append(createTime(path));
		builder.append(" ");
		builder.append(file.getName());

		list.add(builder.toString());

		return FileVisitResult.CONTINUE;
	}

	/**
	 * Metoda stvara vrijeme izdrade i pretvara ga u zadani format(yyyy-MM-dd
	 * HH:mm:ss)
	 * 
	 * @param path
	 *            - put do datoteke
	 * @return vrijeme izrade
	 */
	private String createTime(Path path) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
					LinkOption.NOFOLLOW_LINKS);
			BasicFileAttributes attributes = faView.readAttributes();
			FileTime fileTime = attributes.creationTime();
			return sdf.format(new Date(fileTime.toMillis()));
		} catch (Exception ignorable) {
		}

		return null;
	}

	/**
	 * Metoda vraća listu svih datoteka s opisom
	 * 
	 * @return lista datoteka
	 */
	public List<String> getList() {
		return list;
	}

}
