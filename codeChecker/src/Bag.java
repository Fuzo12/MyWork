import java.util.Arrays;
import java.util.Iterator;

/**
 * Classe associada a criacao e manipulacao de Bags.
 * @param <T> Tipo generico que pode ser atribuido aos elementos contidos dentro da Bag.
 * @author Claudio Coelho
 */
public class Bag<T> implements Iterable<T>{
	/**
	 * Items contidos na Bag.
	 */
	private T[] items;
	/**
	 * Tamanho da Bag.
	 */
	private int size;
	
	/**
	 * Construtor da classe Bag.
	 */
	@SuppressWarnings("unchecked")
	public Bag(){
	items = (T[]) new Object[1];
	size = 0;
	}
	
	/**
	 * Metodo que permite alterar a capacidade de uma Bag.
	 * @param capacity Nova capacidade da Bag.
	 */
	private void resize(int capacity) {
		items = Arrays.copyOf(items, capacity);
	}
	
	/**
	 * Metodo que permite adicionar um elemento a Bag.
	 * @param x Elemento a adicionar a Bag.
	 */
	public void add(T x){
		if (size == items.length)
		resize(2 * items.length);
		items[size++] = x;
		}
	
	/**
	 * Metodo que permite obter um item da Bag.
	 * @param key Indice do elemento da Bag.
	 * @return Elemento da Bag.
	 */
	public T get(int key) {
		return items[key];
	}
	
	/**
	 * Metodo que permite obter o tamanho da Bag.
	 * @return Tamanho da Bag.
	 */
	public int size(){
		return this.size;
		}
	
	/**
	 * Metodo que verifica se a Bag esta vazia.
	 * @return True se a Bag estiver vazia e False se a Bag tiver elementos.
	 */
	public boolean isEmpty(){
		return size == 0;
		}
	
	/**
	 * Coloca os elementos de um array numa Bag.
	 * @param newBag Array de elementos a colocar na Bag.
	 */
	public void refill(T[] newBag) {
		this.items = Arrays.copyOf(newBag, this.size);
	}
	
	/**
	 * Classe associada ao iterador da classe Bag.
	 */
	private class BagIterator implements Iterator<T> {
		/**
		 * Elemento atual do iterador.
		 */
		private int i = 0;
		
		/**
		 * Metodo que verifica se ha mais um elemento na Bag.
		 * Retorna verdadeiro se o elemento atual do iterador for menor que o tamanho da Bag, e falso se for igual ao tamanho da Bag.
		 */
		public boolean hasNext(){
			return i < size;
		}
		
		/**
		 * Permite obter o proximo elemento da Bag.
		 */
		public T next(){
			return items[i++];
		}
		
		/**
		 * Metodo associado a remocao de elementos da Bag.
		 * Para Bags este metodo nao e suportado, quando este metodo e utilizado o mesmo lanca uma excecao.
		 */
		public void remove(){
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * Metodo associado ao iterador da Bag.
	 */
	public Iterator<T> iterator(){
		return new BagIterator();
		}
	
	/**
	 * Converte a Bag numa String para ser imprimida.
	 */
	@Override
	public String toString() {
		String result="[";
		for(int i=0;i<this.items.length;i++) {
			if(this.items[i]!=null) {
				result += this.items[i];
				if(i<items.length-1 && items[i+1]!=null) {result += ",";}	
			}
		}
		result += "]";
		return result;
	}
}
