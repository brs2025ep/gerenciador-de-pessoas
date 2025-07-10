import { useState } from 'react'
import './App.css'
import './styles.css';
import CadastroPage from './CadastroPage'
import PessoasCadastradasList from './component/PessoasCadastradasList';
import PessoasIntegradasSearch from './component/PessoasIntegradasSearch';
import apiService from './services/apiService';

function App() {
  const [pessoaToEdit, setPessoaToEdit] = useState(null);
  const [mockPessoasList, setMockPessoasList] = useState([]);
  const [currentPessoaIntegrada, setCurrentPessoaIntegrada] = useState(null);

  const loadMockPessoas = () => {
    const mockData = [
      {
        id: 1,
        nome: 'John Doe',
        nascimento: '01/01/1995',
        cpf: '00000000000',
        email: 'mail51@mail',
        cep: '02040001',
        rua: 'Conjunto',
        numero: '23',
        cidade: 'Manaus',
        estado: 'Amazonas',
        status: 'Sucesso',
      },
      {
        id: 2,
        nome: 'Felipe Nery',
        nascimento: '01/01/1994',
        cpf: '00000030000',
        email: 'mail2@mail',
        cep: '00000205',
        rua: 'Praça',
        numero: '23',
        cidade: 'São Paulo',
        estado: 'São Paulo',
        status: 'Pendente',
      },
      {
        id: 3,
        nome: 'Matheus Smith',
        nascimento: '01/01/1998',
        cpf: '00000500000',
        email: 'mail@mail',
        cep: '00000001',
        rua: 'Rua',
        numero: '23',
        cidade: 'Curitiba',
        estado: 'Paraná',
        status: 'Sucesso',
      },
      {
        id: 4,
        nome: 'Maria Silva',
        nascimento: '15/07/1990',
        cpf: '11111111111',
        email: 'mail@mail',
        cep: '00000001',
        rua: 'Avenida',
        numero: '30',
        cidade: 'Rio de Janeiro',
        estado: 'Rio de Janeiro',
        status: 'Erro',
      },
    ];
    setMockPessoasList(mockData);
    console.log('Pessoas mockadas carregadas com sucesso!');
  };

  const fetchPessoas = async () => {
    const response = await apiService.getAllPessoas();
    const newPessoasList = response.data;
    console.log(newPessoasList);

    setMockPessoasList(newPessoasList);
  };

  const handleSearchPessoaIntegrada = async (cpf) => {
    console.log("Buscando pessoa Integrada de cpf: " + cpf);

    const response = apiService.getPessoaIntegrada(cpf);
    const pessoaIntegrada = response.data


    setCurrentPessoaIntegrada(pessoaIntegrada);
  }

  const handleGetPessoaToEdit = async (pessoaId) => {
    // const pessoa = mockPessoasList.find((p) => p.id === pessoaId);

    // Obter dados atualizados da pessoa antes de editar
    const response = apiService.getPessoaById(pessoaId);
    const pessoaFromRemote = response.data;

    setPessoaToEdit(pessoaFromRemote);
  };

  const handleDeletePessoa = (pessoaId) => {
    console.log("Removendo pessoa de id:" + pessoaId);
    const selectedPessoa = mockPessoasList.find((p) => p.id === pessoaId);


    try {


      if (selectedPessoa.status === 'Sucesso') {
        console.log("Tentando remover pessoa pelo cpf :" + selectedPessoa.cpf);

        // TODO: Remover pessoa integrada
        const response = apiService.deletePessoaIntegrada(cpfPessoaIntegrada);


      } else {
        // TODO: Remover pessoa cadastrada
        const response = apiService.getPessoaById(pessoaId);
      }

    } catch (error) {
      console.log(error);
    };


    console.log(`Pessoa com ID ${pessoaId} removida.`);
  };

  const handleIntegrarPessoa = (pessoaId) => {
console.log("Tentando integrar pessoa de id: ", pessoaId);

    try {
      const response = apiService.getPessoaById(pessoaId);
      const dataPessoaFromRemote = response.data;

      const submitResponse = apiService.updatePessoa(
        pessoaId,
        dataPessoaFromRemote)

      if (submitResponse.status === 200) {
        // Se a integração foi bem sucedidade, deve atualizar a lista de Pessoas.
        fetchPessoas();
      } else {
        console.log("Ocorreu algo inesperado ao tentar integrar pessoa", pessoaId);
      }
    } catch (error) {
      console.log(error);
    };
  }

  const handleSubmitPessoa = async (editedPessoa) => {
    try {
      console.log("Pessoa cadastrada do App!");
      if (editedPessoa.id) {
        if (editedPessoa.id != -1) {
          await apiService.updatePessoa(editedPessoa.id, editedPessoa);
        }
        console.log('Pessoa atualizada:', editedPessoa);
        setMockPessoasList(
          mockPessoasList.map((pessoa) =>
            pessoa.id === editedPessoa.id ? editedPessoa : pessoa
          )
        );
      } else {
        const response = await apiService.createPessoa(editedPessoa);
        console.log('Pessoa cadastrada remotamente:', response.data);

        let newId = Math.max(...mockPessoasList.map((p) => p.id)) + 1;

        if (mockPessoasList.length === 0) {
          newId = 1;
        }

        const defaultStatus = 'Pendente';
        setMockPessoasList([...mockPessoasList, { ...editedPessoa, id: newId, status: defaultStatus }]);
      }

      console.log("Pessoas length: ", mockPessoasList.length);
      setPessoaToEdit(null);
    } catch (error) {
      console.error('Erro ao salvar pessoa:', error);
      ('Erro ao salvar pessoa. Tente novamente.');
    }
  };

  return (
    <>
      <div className="container">
        <CadastroPage
          pessoaToEdit={pessoaToEdit}
          onSubmit={handleSubmitPessoa}
        />
        <button
          onClick={() => loadMockPessoas(null)}>Load Mock Pessoas (Local)</button>
        <button
          onClick={() => fetchPessoas(null)}>Fetch Pessoas (Remote)</button>
        <PessoasCadastradasList
          pessoas={mockPessoasList}
          onEdit={handleGetPessoaToEdit}
          oDelete={handleDeletePessoa}
          onIntegrar={handleIntegrarPessoa}
        />
        <PessoasIntegradasSearch
          pessoaIntegrada={currentPessoaIntegrada}
          onSearch={handleSearchPessoaIntegrada} />
      </div>
    </>
  )
}

export default App
