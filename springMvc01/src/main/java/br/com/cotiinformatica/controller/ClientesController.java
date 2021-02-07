package br.com.cotiinformatica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.cotiinformatica.entities.Cliente;
import br.com.cotiinformatica.repositories.interfaces.IClienteRepository;

@Controller
public class ClientesController {
	
	@Autowired
	private IClienteRepository clienteRepository;

	// m�todo para abrir a p�gina de cadastro de clientes
	@RequestMapping("/formularioCliente")
	public ModelAndView formularioCliente() {

		// definindo o caminho da p�gina que o m�todo ir� acessar atraves da ROTA..
		ModelAndView modelAndView = new ModelAndView("clientes/formulario-cliente");

		// enviando para a p�gina uma inst�ncia de cliente
		modelAndView.addObject("cliente", new Cliente());

		return modelAndView;
	}

	// m�todo para receber o SUBMIT POST do formul�rio
	@RequestMapping(value = "/cadastrarCliente", method = RequestMethod.POST)
	public ModelAndView cadastrarCliente(Cliente cliente, ModelMap map) {
				
		try {
			
			//verificando se o email informado j� encontra-se cadastrado..
			if(clienteRepository.findByEmail(cliente.getEmail()) != null) {
				throw new Exception("O email " + cliente.getEmail() + ", j� encontra-se cadastrado. Tente outro.");
			}
			
			//gravando o objeto cliente no banco de dados
			clienteRepository.create(cliente);
			
			// definindo uma mensagem para exibir na p�gina..
			map.addAttribute("mensagem_sucesso", "Cliente " + cliente.getNome() + ", cadastrado com sucesso.");
		}
		catch(Exception e) {
			map.addAttribute("mensagem_erro", e.getMessage());
		}	
		
		// criando um objeto ModelAndView
		ModelAndView modelAndView = new ModelAndView("clientes/formulario-cliente", map);
		// enviando para a p�gina uma inst�ncia de cliente
		modelAndView.addObject("cliente", new Cliente());
		return modelAndView;
	}

	// m�todo para abrir a p�gina de consulta de clientes
	@RequestMapping("/listagemClientes")
	public ModelAndView listagemClientes(ModelMap map) {
				
		// WEB-INF/views/clientes/listagem-clientes.jsp
		ModelAndView modelAndView = new ModelAndView("clientes/listagem-clientes");
		
		try {
			//executando a consulta de clientes no banco de dados (reposit�rio)
			// e envia-la para a p�gina atraves da classe ModelAndView
			modelAndView.addObject("clientes", clienteRepository.findAll());
			
		}catch(Exception e) {
			//mensagem de erro..
			map.addAttribute("mensagem_erro",e.getMessage());
		}
				
		return modelAndView;
	}
	
	//m�todo para realizar a a��o de exclus�o
	@RequestMapping("/excluirCliente")
	public ModelAndView excluirCliente(Integer id, ModelMap map) {
		
		ModelAndView modelAndView = new ModelAndView("clientes/listagem-clientes");
		
		try {
			//buscar o cliente no banco de dados atraves do id..
			Cliente cliente = clienteRepository.findById(id);
			
			//verificar se o cliente foi encontrado..
			if(cliente !=null) {
				//excluindo o cliente..
				clienteRepository.delete(id);
				
				//gerando mensagem..
				map.addAttribute("mensagem_sucesso", "Cliente " + cliente.getNome() + ", exclu�do com sucesso.");
			}
			else {
				map.addAttribute("mensagem_erro", "Cliente n�o encontrado.");
			}
			
			//executando a consulta de clientes para exibir na p�gina de listagem..
			modelAndView.addObject("clientes", clienteRepository.findAll());
		}
		catch(Exception e) {
			map.addAttribute("mensagem_erro", e.getMessage());
		}
		
		return modelAndView;
	}
	
	//m�todo para abrir a p�gina de edi��o do cliente
	@RequestMapping("edicaoCliente")
	public ModelAndView edicaoCliente(Integer id, ModelMap map) {
		
		ModelAndView modelAndView = new ModelAndView("clientes/edicao-clientes");
		
		try {
			
			//obter os dados cliente no repositorio (findById)
			Cliente cliente = clienteRepository.findById(id);
			
			if(cliente != null) { //verificando se o cliente foi encontrado..
				
				//enviando um objeto cliente para a p�gina (j� preenchido)
				modelAndView.addObject("cliente", cliente);
			}
			else {
				map.addAttribute("mensagem_erro", "Cliente n�o encontrado."); // enviando mensagem de erro
				
				modelAndView.setViewName("clientes/listagem-clientes"); //enviado para p�gina apos nao encontrar o cliente
				modelAndView.addObject("clientes", clienteRepository.findAll()); // exibbindo a lista de clientes 
			}
			
		}catch(Exception e) {
			map.addAttribute("mensagem_erro", e.getMessage());
		}
		
		
		return modelAndView;
	}
	
	//M�todo para processar o SUBMIT POST do formulario de edi��o de cliente..
	@RequestMapping(value="atualizarCliente", method = RequestMethod.POST)
	public ModelAndView atualizarCliente(Cliente cliente, ModelMap map) {
		
		ModelAndView modelAndView = new ModelAndView("clientes/edicao-clientes");
		
		try {
			//verificar se o email informado ja encontra-se cadastrado..
			Cliente registro = clienteRepository.findByEmail(cliente.getEmail());
			
			//verificar se o cliente encontrado � outro cliente (com o mesmo email)
			if(registro != null && !registro.equals(cliente)) {
				map.addAttribute("mensagem_erro", "O email informado j� encontra-se cadastrado para outro cliente.");
			}
			else {
				//atualizando o cliente..
				clienteRepository.update(cliente);				
				//mensagem de sucesso..
				map.addAttribute("mensagem_sucesso", "Cliente atualizado com sucesso.");
				
			}
			
			modelAndView.addObject("cliente", cliente);
			
			
		}catch(Exception e) {
			map.addAttribute("mensagem_erro", e.getMessage());
		}
				
		return modelAndView;
	}
}