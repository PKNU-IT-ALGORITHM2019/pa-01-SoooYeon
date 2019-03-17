
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Assignment01 {

	public static int BUFFER_SIZE = 200000;
	public static int size = 0;
	public static boolean findName = false;
	public static Dict [] dict = new Dict[BUFFER_SIZE];
	public static void main(String[] args) {
		String command = null; 
		String readfile = null;
		Scanner kb = new Scanner(System.in);
		while(true) {
			String data=null;
			System.out.print("$ ");
			command = kb.next();			
			if(command.equals("read")) { 
				readfile = kb.next();
				DictRead(readfile);}
			else if(command.equals("size"))
				getSize();
			else if(command.equals("find")) {		
				data=kb.nextLine();
				data = data.substring(1);
				findWord(data);
			}
			else if(command.equals("exit")) break;
			else break;

		}kb.close();
	}
	public static void getSize()
	{
		System.out.println(size);
	}
	public static void DictRead(String readfile)
	{				
		int i=1;
		int samename=1;
		FileReader f_reader = null;
		try {
			f_reader = new FileReader(readfile);
			BufferedReader inFile = new BufferedReader(f_reader);
			while(true) {
				if(size >= BUFFER_SIZE || samename >= BUFFER_SIZE) reallocate();
				String tmp=null;
				tmp = inFile.readLine();
				if(tmp==null) break;
				String Rname = null;
				String name = null;
				String cont = null;
				if(tmp.trim().equals("")) continue;
				name=tmp.substring(0, tmp.indexOf(" ("));
				cont=tmp.substring(tmp.indexOf(" (")+1,tmp.length());
				Rname = Replace(name);				
				dict[size]=new Dict(name,cont,samename,Rname);
				if(size!=0 && dict[size-1].name.equalsIgnoreCase(name)) {							
					dict[size-i].samename++;
					i++;
				}
				else i=1;
				size++;
			}
			inFile.close();
			f_reader.close();

		}catch(FileNotFoundException e){e.printStackTrace();}
		catch(IOException e) {e.printStackTrace();}
	}
	public static void reallocate()
	{
		Dict [] tmp = new Dict[BUFFER_SIZE*2];
		for(int i=0;i<BUFFER_SIZE;i++) 
			tmp[i] = dict[i];
		dict = tmp;
		BUFFER_SIZE *=2;
	}
	public static boolean getRname = false;
	public static int getFind(String target,int begin, int end)
	{
		String realDict = null;
		if(begin>end && end==0) { return -1;}
		else if (begin>end){ return end;}
		else {
			int middle=(begin+end)/2;
			if (getRname) realDict = dict[middle].Rname;
			else realDict = dict[middle].name;
			if(realDict.equalsIgnoreCase(target)) {
				getRname = false; findName = true; return middle;}
			else if (realDict.compareToIgnoreCase(target)>0) 
				return getFind(target,begin,middle-1);
			else 
				return getFind(target,middle+1,end);
		}
	}
	public static void findWord(String data)
	{
		int indexfind=0;
		String Rdata=null;
		Rdata=Replace(data);
		if (data.equalsIgnoreCase(Rdata)) 
			indexfind = getFind(data,0,size-1);
		else {getRname = true;
			indexfind = getFind(Rdata,0,size-1);
		}
		if (!findName) {
			System.out.println("Not Found.");
			if (indexfind==-1) return;
			System.out.println(dict[indexfind].name+" "+dict[indexfind].cont.substring(0, dict[indexfind].cont.indexOf(")")+1));
			System.out.println("- - -");
			if(indexfind==size-1)return;
			System.out.println(dict[indexfind+1].name+" "+dict[indexfind+1].cont.substring(0, dict[indexfind+1].cont.indexOf(")")+1));
		}
		else {
			int flagindex = indexfind;
			int j=1;
			while(true) {
				if(flagindex<=0||!dict[flagindex-j].name.equalsIgnoreCase(dict[flagindex].name)) break;
				flagindex-=j;
			}
			System.out.println("Found "+dict[flagindex].samename+" items.");
			for(int i=0; i<dict[flagindex].samename; i++)
				System.out.println(dict[flagindex+i].name+" "+dict[flagindex+i].cont);
			findName = false;
		}
	}
	public static String Replace(String data) 
	{
		data = data.replaceAll("-", "");
		data = data.replaceAll("'", "");
		data = data.replaceAll(" ", "");
		return data;
	}

}
