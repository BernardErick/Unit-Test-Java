package br.com.aula;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import br.com.aula.exception.*;
import org.junit.Assert;
import org.junit.Test;

public class BancoTest {

	@Test // Item A.a
	public void deveCadastrarConta() throws ContaJaExistenteException, ContaComNumeroInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		assertEquals(1, banco.obterContas().size());
	}

	@Test(expected = ContaJaExistenteException.class) //Item A.b
	public void naoDeveCadastrarContaNumeroRepetido() throws ContaJaExistenteException, ContaComNumeroInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}
	@Test(expected = ContaJaExistenteException.class) //Item A.d
	public void naoDeveCadastrarContaNomeRepetido() throws ContaJaExistenteException, ContaComNumeroInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 122, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Joao");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}
	@Test(expected = ContaComNumeroInvalidoException.class) //Item A.c
	public void naoDeveCadastrarContaComNumeroInvalido() throws ContaComNumeroInvalidoException, ContaJaExistenteException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, -123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();
		// Ação
		banco.cadastrarConta(conta1);

		Assert.fail();
	}
	@Test
	public void deveEfetuarTransferenciaContasCorrentes() throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaNegativa {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}
	@Test //Item B.a
	public void deveEfetuarTransferenciaContasCorrentePoupanca() throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaNegativa {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}
	@Test //Item B.b
	public void deveVerificarExistenciaDaContaOrigem() throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaNegativa {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Verificação
		assertEquals(123, banco.obterContaPorNumero(contaOrigem.getNumeroConta()).getNumeroConta());

		// Ação
		banco.efetuarTransferencia(123, 456, 100);
	}
	@Test(expected = ContaSemSaldoException.class) //Item B.c
	public void naoDevePermitirContaOrigemPoupancaSemSaldo() throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaNegativa {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		Assert.fail();
	}
	@Test //Item B.d
	public void deveVerificarExistenciaDaContaDestino() throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaNegativa {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Verificação
		assertEquals(456, banco.obterContaPorNumero(contaDestino.getNumeroConta()).getNumeroConta());

		// Ação
		banco.efetuarTransferencia(123, 456, 100);
	}
	@Test(expected = TransferenciaNegativa.class) //Item B.e
	public void naoDevePermitirTransferirValorNegativo() throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaNegativa {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, -10);

		Assert.fail();
	}
}
