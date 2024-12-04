package exemplo;



class SearcherThread extends Thread {
	private TextRepository textRepository;

	public SearcherThread(TextRepository textRepository) {
		this.textRepository = textRepository;
	}

	@Override
	public void run() {
		TextChunk c = textRepository.getChunk();
		while(c != null) {
			int p = c.text.indexOf(c.stringToBeFound);
			while(p != -1) {	
				System.out.println("Encontrado em " + (c.getInitialPos() + p));
				c.addFoundPos(p);
				textRepository.submitResult(c);
				p = c.text.indexOf(c.stringToBeFound, p+c.stringToBeFound.length());
				if(p != -1)
					System.out.println("Found twice");
			}
			c = textRepository.getChunk();
		}
			
		
		
	}
}
