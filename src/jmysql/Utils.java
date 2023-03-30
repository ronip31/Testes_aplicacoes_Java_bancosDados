package jmysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Utils {
	
	static Scanner teclado = new Scanner(System.in);
	
	public static Connection conectar () {
		//System.out.println("Conectando...");
		String CLASSE_DRIVER = "com.mysql.cj.jdbc.Driver";
		String USUARIO = "geek";
		String SENHA = "gesplan";
		String URL_SERVIDOR = "jdbc:mysql://DESKTOP-TKCV6QN:3306/jmysql?useSSL=False";
		
		try {
			Class.forName(CLASSE_DRIVER);
			return DriverManager.getConnection(URL_SERVIDOR, USUARIO, SENHA);
		}catch (Exception e) {
			if(e instanceof ClassNotFoundException) {
				System.out.println("Verifiqe o driver de conexao.");
			} else {
				System.out.println("Verifique se o servidor está ativo.");
			}
			System.exit(-42);
			return null;
		}
	}
	
	public static void desconectar(Connection conn) {
		//System.out.println("Desconectando...");
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Não foi possivel fechar a conexão.");
				e.printStackTrace();
				}
			}
		}
		
	public static void listar() {
		//System.out.println("Listando produtos...");
		String BUSCAR_TODOS = "SELECT * FROM produtos";
		
		try {
			Connection conn = conectar();
			PreparedStatement produtos = conn.prepareStatement(BUSCAR_TODOS, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet res = produtos.executeQuery();
			
			
			res.last();
			int qtd = res.getRow();
			res.beforeFirst();
			
			if(qtd > 0) {
				System.out.println("Listando Produtos...");
				System.out.println("---------------------------");	
				
				while (res.next()) {
					System.out.println("ID: " + res.getInt(1));
					System.out.println("Produto: " + res.getString(2));
					System.out.println("Preco: " + res.getFloat(3));
					System.out.println("Estoque: " + res.getInt(4));
					System.out.println("---------------------------");
					
				}
			}else {
				System.out.println("Nao existem produtos cadastrados.");
			}
			produtos.close();
			desconectar(conn);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Erro buscando produtos.");
			System.exit(-42);
		}
	}
	
	public static void inserir() {
		//System.out.println("Inserindo produtos...");
		System.out.println("Informe o nome do Produto: ");
		String nome = teclado.nextLine();
		
		System.out.println("Informe o preco do produdo: ");
		float preco = Float.parseFloat(teclado.next());
		
		System.out.println("Informe a quantidade em estoque: ");
		int estoque = teclado.nextInt();
		
		String INSERIR = "INSERT INTO produtos(nome, preco, estoque) VALUES (?, ?, ?)";
		// SQL INJECTION
		try{
			Connection conn = conectar();
			PreparedStatement salvar = conn.prepareStatement(INSERIR);
			
			salvar.setString(1, nome);
			salvar.setFloat(2, preco);
			salvar.setInt(3, estoque);
			
			salvar.executeUpdate();
			salvar.close();
			desconectar(conn);
			
			System.out.println("O produto " +  nome + "  " + "  foi inserido com sucesso.");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro salvando produtos");
			System.exit(-42);
		}
		
	}
	
	public static void atualizar() {
		System.out.println("informe o cdigo do produto: ");
		int id =  Integer.parseInt(teclado.nextLine());
		
		String BUSCAR_POR_ID = "SELECT * FROM produtos WHERE id=?";
		
		try {
			Connection conn = conectar();
			PreparedStatement produto = conn.prepareStatement(BUSCAR_POR_ID);		
			produto.setInt(1, id);
			ResultSet res = produto.executeQuery();
			
			res.last();
			int qtd = res.getRow();
			res.beforeFirst();
			
			if(qtd > 0) {
				System.out.println("Informe o nome do Produto: ");
				String nome = teclado.nextLine();
				
				System.out.println("Informe o preco do produto: ");
				float preco = Float.parseFloat(teclado.nextLine());
				
				System.out.println("Informe a quantidade do produto: ");
				int estoque = teclado.nextInt();
				
				String ATUALIZAR = "UPDATE produto SET nome=?, preco=?, estoque=? WHERE id=?";
				PreparedStatement upd = conn.prepareStatement(ATUALIZAR);
				
				upd.setString(1, nome);
				upd.setFloat(2, preco);
				upd.setInt(3, estoque);
				upd.setInt(4, id);
				
				upd.executeUpdate();
				upd.close();
				desconectar(conn);
				System.out.println("O produto " + nome + " foi atualizado com sucesso");
				
			}else {
				System.out.println("Nao existe produto com o id informado");
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao atualizar produto");
			System.exit(-42);
		}
		
	}
	
	public static void deletar() {
		System.out.println("Deletando produtos...");
	}
	
	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma opção: ");
		System.out.println("1 - Listar produtos.");
		System.out.println("2 - Inserir produtos.");
		System.out.println("3 - Atualizar produtos.");
		System.out.println("4 - Deletar produtos.");
		
		int opcao = Integer.parseInt(teclado.nextLine());
		if(opcao == 1) {
			listar();
		}else if(opcao == 2) {
			inserir();
		}else if(opcao == 3) {
			atualizar();
		}else if(opcao == 4) {
			deletar();
		}else {
			System.out.println("Opção inválida.");
		}
	}
}
