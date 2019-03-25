package dk.easv.friendsv2.Model;

import java.util.ArrayList;

import dk.easv.friendsv2.R;

public class Friends {

	ArrayList<BEFriend> m_friends;
	
	public Friends()
	{
		m_friends = new ArrayList<BEFriend>();
		m_friends.add(new BEFriend("Jacob", "52525308", "wiktoria.liskiewicz@gmail.com", 55.620532, 8.481840, true, R.drawable.sam));
		m_friends.add(new BEFriend("Anni", "52525311","andersen19@gmail.com", 55.479827, 8.479160, R.drawable.jacob));
		m_friends.add(new BEFriend("Samuel", "126256","samu1667@easv365.dk", 55.479827, 8.479160, true, R.drawable.sam));
		m_friends.add(new BEFriend("Samulus", "234567", "samu1667@easv365.dk", 55.479827, 8.479160, R.drawable.sam));
		m_friends.add(new BEFriend("Attila", "123456", "jaco4998@easv365.dk", 55.479827, 8.479160, R.drawable.jacob));
		m_friends.add(new BEFriend("Bastian", "994567", "kasp357f@easv365.dk", 55.479827, 8.479160, R.drawable.jacob));
		m_friends.add(new BEFriend("Bence", "127426", "lalala@gmail.com", 55.479827, 8.479160, R.drawable.jacob));
		m_friends.add(new BEFriend("Bo", "204587", "lalala@gmail.com", 55.479827, 8.479160, true, R.drawable.sam));
		m_friends.add(new BEFriend("Daniel", "123456", "lalala@gmail.com", 55.479827, 8.479160, R.drawable.sam));
	}
	
	public ArrayList<BEFriend> getAll()
	{ return m_friends; }
	
	public String[] getNames()
	{
		String[] res = new String[m_friends.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = m_friends.get(i).getName();
		}
		return res;
	}

}
