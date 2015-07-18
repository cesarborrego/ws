package c.neo.placas_validator;

import java.util.ArrayList;
import java.util.List;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

public class NdefMessageParser {
	private NdefMessageParser()
	{
		
	}
	
	public static List<TextRecord> parse(NdefMessage message)
	{
		List<TextRecord> textRecords = new ArrayList<TextRecord>();
		
		//Get the Records inside the message
		NdefRecord[] records = message.getRecords();
		
		//Iterate through and generate a list of text records
		if(records != null && records.length>0)
		{
			for(NdefRecord local: records)
			{
				TextRecord textRecord = TextRecord.parse(local);
				textRecords.add(textRecord);
			}
		}
		
		return textRecords;
	}
}
