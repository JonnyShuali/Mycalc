package com.roy.mycalc;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView outResult; // The text being view on the screen
	static ArrayList<Integer> op_index; // Keep the all op been used
	static boolean isFirst;
	static boolean isError;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		outResult = (TextView) findViewById(R.id.result);
		outResult.setText("");// Set the result text to be empty
		op_index = new ArrayList<Integer>();
		isFirst = true;
		isError = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * checks the number wont start with zero
	 */
	public void zero_click(View sender) {
		char last = lastChar();
		if (last == '/' || last == '+' || last == '-' || last == '*'
				|| last == '/')
			return;
		num_click(sender);
	}

	/**
	 * Program runs each time button is being clicked
	 * 
	 * @param sender
	 *            - the button
	 */
	public void num_click(View sender) {
		if (isError == true)
			reset_click(sender);
		Button num = (Button) sender;
		outResult.append(num.getText());
		isFirst = false;
	}

	public void op_click(View sender) {
		if (isError
				|| isFirst
				|| !op_index.isEmpty()
				&& op_index.get(op_index.size() - 1) == outResult.getText()
						.toString().length() - 1)
			return;
		op_index.add(outResult.getText().length());
		num_click(sender);
	}

	/**
	 * @return last char in the result text view
	 */
	private char lastChar() {
		if (outResult.getText().toString().equals(""))
			return '/';
		return outResult.getText().charAt(outResult.getText().length() - 1);

	}

	/**
	 * checks if the number had a point before
	 */
	public void point_click(View sender) {
		if (!op_index.isEmpty()
				&& outResult.getText().toString()
						.substring(op_index.get(op_index.size() - 1))
						.contains(".")) {
			return;
		}
		if (isError)
			reset_click(sender);
		num_click(sender);
	}

	public void calcu_click(View sender) {
		String st = outResult.getText().toString();
		int stIndex;
		if (op_index.isEmpty() || st.charAt(st.length() - 1) == '+'
				|| st.charAt(st.length() - 1) == '-'
				|| st.charAt(st.length() - 1) == '*'
				|| st.charAt(st.length() - 1) == '/') {
			outResult.setText("ERROR");
			isError = true;
			return;
		}

		// ((TextView)findViewById(R.id.bla)).setText(op_index.toString());
		for (int i = 0; i < op_index.size(); i++) {
			if (isError) {
				outResult.setText("ERROR");
				isError = true;
				return;
			}
			stIndex = -1;
			System.out.println(st.indexOf('*'));
			int indexmulti = st.indexOf('*');
			int indexdivide = st.indexOf('/');
			int indexplus = st.indexOf('+');
			int indexminus = st.indexOf('-');
			stIndex = Math.min(indexmulti, indexdivide);
			if (stIndex == -1)
				stIndex = Math.min(indexplus, indexminus);
			calculate(stIndex, outResult.getText().toString().charAt(stIndex));
			System.out.println("test/");
			System.out.println("stindex=" + stIndex);
			st = outResult.getText().toString();
		}
	}

	private void calculate(int stIndex, char op) {
		int arrIndex = op_index.indexOf(stIndex);
		double beforeNum, afterNum;
		((TextView) findViewById(R.id.bla)).setText(op_index.toString());
		if (op_index.size() == 1) {
			beforeNum = Double.parseDouble(outResult.getText().toString()
					.substring(0, stIndex - 1));
			afterNum = Double.parseDouble(outResult.getText().toString()
					.substring(stIndex + 1));
		} else {
			// Search and convert the number before
			beforeNum = Double.parseDouble(outResult.getText().toString()
					.substring(op_index.get(arrIndex - 1) + 1, stIndex - 1));
			// Search and convert the number after
			afterNum = Double.parseDouble(outResult.getText().toString()
					.substring(stIndex + 1, op_index.get(arrIndex + 1) - 1));
		}
		((TextView) findViewById(R.id.bla)).setText(beforeNum + " " + afterNum);
		// replace the result in the string
		double result;
		switch (op) {
		case '+':
			result = beforeNum + afterNum;
			break;
		case '-':
			result = beforeNum - afterNum;
			break;
		case '*':
			result = beforeNum * afterNum;
			break;
		case '/':
			result = beforeNum / afterNum;
			break;
		default:
			isError = true;
			return;
		}
		System.out.print("here");
		outResult.setText(outResult
				.getText()
				.toString()
				.replaceFirst(
						outResult
								.getText()
								.toString()
								.substring(op_index.get(arrIndex - 1) + 1,
										op_index.get(arrIndex + 1) - 1),
						Double.toString(result)));

	}

	/**
	 * delete the last character from the result textview
	 */
	public void delete_click(View sender) {
		if (outResult.getText().toString().equals(""))
			return;
		if (isError) {
			reset_click(sender);
			return;
		}
		// If it an op - delete it from the vector as well
		switch (outResult.getText().toString()
				.charAt(outResult.getText().length() - 1)) {
		case '+':
		case '-':
		case '*':
		case '/':
			op_index.remove(op_index.size() - 1);
		}
		outResult.setText(outResult.getText().toString()
				.substring(0, outResult.getText().toString().length() - 1));
	}

	/**
	 * Reset the calculator
	 */
	public void reset_click(View sender) {
		outResult.setText("");// Set the result text to be empty
		op_index.clear();
		isFirst = true;
		isError = false;
	}

}
