package it.polito.tdp.PremierLeague.model;

public class Simulator {
	
	private int N;
	private Match m;
	private Model model;
	private int nCasa;
	private int nOspiti;
	private int golCasa;
	private int golOspiti;
	
	private int squadraMigliore;
	
	
	public Simulator(int n, Match m, Model model) {
		super();
		N = n;
		this.m = m;
		this.model = model;
	}
	
	public void init() {
		this.nCasa=11;
		this.nOspiti=11;
		this.golCasa=0;
		this.golOspiti=0;
		Player giocatoreMigliore=this.model.getGiocatoreMigliore().getP();
		squadraMigliore=this.model.getSquadraGiocatoreMigliore(giocatoreMigliore);
		
		
		
	}
	
	public void sim() {
		for(int i=0; i<this.N; i++) {
			double prob=Math.random()*100.0;
			if(prob<=50.0) {
				//è stato segnato un gol
				
				if(this.nCasa>this.nOspiti)
					this.golCasa++;
				else if(this.nOspiti>this.nCasa)
					this.golOspiti++;
				else {
					
					
					
					if(this.squadraMigliore==this.m.getTeamHomeID())
						this.golCasa++;
					else
						this.golOspiti++;
				}
			}
			
			if(prob>50.0 && prob<=80.0) {
				//espulsione
				double probS=Math.random()*100.0;
				
				if(probS<=60.0) {
					//espulso giocatore della squadra con giocatore migliore
					
					
					if(this.squadraMigliore==this.m.getTeamHomeID())
						this.nCasa--;
					else
						this.nOspiti--;
				}
				else {
					if(this.squadraMigliore!=this.m.getTeamHomeID())
						this.nCasa--;
					else
						this.nOspiti--;
				}
				
				
			}
			
			if(prob>80.0 && prob<=100.0) {
				//infortunio
				double probR=Math.random()*100.0;
				if(probR<=50.0) {
					this.N=this.N+2;
				}else
					this.N=this.N+3;
			}
			
			
			
		}
		
		System.out.println(this.golCasa+"-"+this.golOspiti);
		System.out.println((11-this.nCasa)+"-"+(11-this.nOspiti));
		
		
	}
	
	public Integer golCasa() {
		return this.golCasa();
	}
	
	public Integer golOspite() {
		return this.golOspite();
	}
	
	public Integer espulsiCasa() {
		return (11-this.nCasa);
		
	}
	
	public Integer espulsiOspiti() {
		return (11-this.nOspiti);
	}
	
	

}
