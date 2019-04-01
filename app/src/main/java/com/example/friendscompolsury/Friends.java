package com.example.friendscompolsury;

import com.example.friendscompolsury.Model.BEFriend;

import java.util.ArrayList;

import dk.easv.friendsv2.R;

public class Friends {

	ArrayList<BEFriend> m_friends;
	
	public Friends()
	{
		m_friends = new ArrayList<BEFriend>();
		m_friends.add(new BEFriend(0, "Jacob","Sky","6666958", "pray@gmail.com", "www.gottasavetheworld.com", "0-0-0000", 0,1,R.drawable.jacob));
		m_friends.add(new BEFriend(1, "Sam","Sky","6666958", "pray@gmail.com", "www.gottasavetheworld.com", "0-0-0000", 0,1,R.drawable.sam));
		m_friends.add(new BEFriend(2, "Kris","Sky","6666958", "pray@gmail.com", "www.gottasavetheworld.com", "0-0-0000", 0,1,R.drawable.jacob));
		}
	
	public ArrayList<BEFriend> getAll()
	{ return m_friends; }
	public void setArraylistFried (ArrayList<BEFriend> arraylistFried)
	{
		m_friends.clear();
		m_friends.addAll(arraylistFried);

	}
	public String[] getNames()
	{
		String[] res = new String[m_friends.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = m_friends.get(i).getM_name();
		}
		return res;
	}
	public Integer[] getImages()
	{
		Integer[] res = new Integer[m_friends.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = m_friends.get(i).getM_img();
		}
		return res;
	}
	public BEFriend[] getFriends()
	{
		BEFriend[] res = new BEFriend[m_friends.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = m_friends.get(i);
		}
		return res;
	}

}
