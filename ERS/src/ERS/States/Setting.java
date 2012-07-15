package ERS.States;

public enum Setting {
	sixNine("69", false),
	kingQueen("KQ", false),
	topBottom("TB", false),
	AISlap("AIS", true),
	AISlapIn("AISI", true),
	AIBurn("AIB", true),
	tenStops("TS", false),
	addToTen("ATT", false),
	sound("S", false),
	hints("H", false);
	
	public boolean on;
	public String name;

	Setting(String name, boolean o){
		on = o;
		this.name = name;
	}
	
	public String getSave(){
		return name + ":" + (on) + " ";
	}
	
	public static void load(String load){
		String[] split = load.split(":");
		
		Setting loadFor = null;
		
		for(Setting s: Setting.values()){
			if(s.name.equalsIgnoreCase(split[0])){
				loadFor = s;
				break;
			}
		}
		
		if(loadFor == null)
			return;
		
		try{
			if(split[1].toLowerCase().startsWith("f")){
				loadFor.on = false;
			}else{
				loadFor.on = true;
			}
		}catch(Exception e){
			loadFor.on = false;
		}
	}
}
