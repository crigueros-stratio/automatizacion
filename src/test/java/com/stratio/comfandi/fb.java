package com.stratio.comfandi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class fb {

	public static void main(String[] args) throws IOException {
		int x = 20;
		while (x > 0) {
			System.out.println(fib(x));
			x = x-1;
		}
	}

	public static int fib(int num) {
		return num <= 1 ? num : fib(num - 1) + fib(num - 2);
	}

}