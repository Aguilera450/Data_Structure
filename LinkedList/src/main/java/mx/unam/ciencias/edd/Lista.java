package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
     	    this.elemento = elemento;
        }
	
	/* Obtiene el elemento en el nodo. */
	private T getElemento() {
	    return elemento;
	}
	
	/* Obtiene el nodo siguiente. */
	private Nodo getSiguiente() {
	    return siguiente;
	}
	
	/* Asigna referencia a nodo siguiente. */
	private void setSiguiente(Nodo siguiente) {
	    this.siguiente = siguiente;
	}
	
	/* Obtiene el nodo anterior. */
	private Nodo getAnterior() {
	    return anterior;
	}
	
	/* Asigna referencia a nodo anterior. */
	private void setAnterior(Nodo anterior) {
	    this.anterior = anterior;
	}
    }
    
    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() throws NoSuchElementException {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            T e = siguiente.elemento;
            anterior = siguiente;
            siguiente = siguiente.siguiente;
            return e;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() throws NoSuchElementException {
            if(!hasPrevious()) {
                throw new NoSuchElementException();
            }
            T e = anterior.elemento;
            siguiente = anterior;
            anterior = anterior.anterior;
            return e;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            siguiente = cabeza;
            anterior = null;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
            siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return getLongitud();
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
	return (cabeza == null && rabo == null) ? true : false;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
	if(elemento == null) throw new IllegalArgumentException();
	longitud++;	
	// Creamos un nodo nuevo.
	Nodo nodo = new Nodo(elemento);
	if(esVacia()) agregaVacia(nodo);
	else {
	    rabo.setSiguiente(nodo);  // El sig. del rabo es el nuevo nodo.
	    nodo.setAnterior(rabo);   // El anterior al nuevo nodo es el rabo.
	    rabo = nodo;
	    rabo.setSiguiente(null);  // En Java esto se puede omitir.
	}

    }
    
    /* Método auxiliar que agrega un nodo cuando la lista es vacia. */
    private void agregaVacia(Nodo nodo) {
	cabeza = nodo;
	rabo = cabeza;
	// En Java esto se puede omitir, esto gracias al recolector de Java.
	cabeza.setSiguiente(null); // Es indistinto usar la cabeza o el rabo
	cabeza.setAnterior(null);  // pues rabo apunta a cabeza.
    }
    
    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
	if(elemento == null) throw new IllegalArgumentException();
	longitud++;
	Nodo nodo = new Nodo(elemento);
	if(esVacia()) agregaVacia(nodo);
	else {
	    cabeza.setAnterior(nodo);
	    nodo.setSiguiente(cabeza);
	    cabeza = nodo;
	    cabeza.setAnterior(null); // En Java esto se puede omitir.
	}
    }
    
    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) throws IllegalArgumentException {
	if (elemento == null) throw new IllegalArgumentException();
	
	if (i < 1) agregaInicio(elemento);
	else if (i > getElementos() - 1) agregaFinal(elemento);
	else {
	    longitud++;
	    Nodo nodo = new Nodo(elemento);
	    Nodo temp = getNodo(i);
	    temp.getAnterior().setSiguiente(nodo);
	    nodo.setAnterior(temp.getAnterior());
	    nodo.setSiguiente(temp);
	    temp.setAnterior(nodo);
	}
    }
    
    /*
     * Regresa primer nodo de la lista que  sea igual al elemento.
     * Si no lo encuentra, regresa null.
     */
    private Nodo buscaNodo(T elemento) {
	int i = longitud;
	Nodo nodo = cabeza;
	while(i-- > 0) {
	    if (nodo.getElemento().equals(elemento)) {
		return nodo;
	    }
	    nodo = nodo.getSiguiente();
	}
	return null;
    }
        
    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
	if(contiene(elemento)) eliminaAux(buscaNodo(elemento));
    }
    
    /* Método auxiliar que elimina el nodo que se pasa como parámetro. */
    private void eliminaAux(Nodo n) {
	if (n != null) {
	    longitud--;
	    if (cabeza == rabo) {
		cabeza = null;
		rabo = null;
	    } else if (n == cabeza) {
		n.getSiguiente().setAnterior(null);
		cabeza = n.getSiguiente();
	    } else if (n == rabo) {
		n.getAnterior().setSiguiente(null);
		rabo = n.getAnterior();
	    } else {
		n.getAnterior().setSiguiente(n.getSiguiente());
		n.getSiguiente().setAnterior(n.getAnterior());
	    }
	}
    }
    
    
    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() throws NoSuchElementException {
	if(esVacia()) throw new NoSuchElementException();
	T e = cabeza.elemento;
	eliminaAux(cabeza);
	return e;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() throws NoSuchElementException {
	if (esVacia()) throw new NoSuchElementException();
        T e = rabo.elemento;
       	eliminaAux(rabo);
	return e;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
	return buscaNodo(elemento) != null;
    }
    
    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
	Lista<T> lista = new Lista<>();
	for(T elem : this)
	    lista.agregaInicio(elem);
	return lista;
    }
    
    /*
     * Regresa el <em>i</em>-ésimo nodo de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de nodos en la lista.
     */
    private Nodo getNodo(int i) throws ExcepcionIndiceInvalido {
	if (i < 0 || i >= longitud) {
	    throw new ExcepcionIndiceInvalido();
	}
	
	Nodo nodo = cabeza;
	while(i-- > 0)
	    nodo = nodo.getSiguiente();
	return nodo;
    }
    
    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
	if (longitud == 0) { return "[]"; }
	String rep = "[";
	Iterator<T> i = iterator();
	for(int c = 0; c < longitud - 1; c++)
	    rep += i.next() + ", ";
	rep += i.next() + "]";
	return rep;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copia de la lista.
     */
    public Lista<T> copia() {
        Lista<T> lista = new Lista<>();
	for(T elem : this)
	    lista.agrega(elem);
	return lista;
    }
    
    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        cabeza.setSiguiente(null);
	rabo.setAnterior(null);
	cabeza = null;
	rabo = null;
	longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() throws NoSuchElementException {
        if(esVacia()) throw new NoSuchElementException();
	return cabeza.getElemento();
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() throws NoSuchElementException {
        if(esVacia()) throw new NoSuchElementException();
	return rabo.getElemento();
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) throws ExcepcionIndiceInvalido {
        if(i < 0 || i > longitud) throw new ExcepcionIndiceInvalido();
    	return getNodo(i).getElemento();
    }
    
    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        int i = 0;
	for(T elem : this)
	    if(elem.equals(elemento)) return i;
	    else i++;
	return -1;
    }
        
    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;

        if(lista.getLongitud() != this.getLongitud()) {
            return false;
        }

        Iterator ia = lista.iteradorLista();
        Iterator ib = this.iteradorLista();
        while(ia.hasNext() && ib.hasNext()) {
            if(!ia.next().equals(ib.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }
}
