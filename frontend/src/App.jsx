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
    const newPessoasList = response;


    setMockPessoasList(newPessoasList);
  };

  const handleEditPessoa = (pessoaId) => {
    const pessoa = mockPessoasList.find((p) => p.id === pessoaId);
    // TODO: Recuperar Pessoa fazendo requisição para a API
    setPessoaToEdit(pessoa);
  };

  const handleDeletePessoa = (pessoaId) => {
    // In a real application, you would call apiService.deletePessoa(pessoaId)
    // For now, we filter it out from the mock list
    setMockPessoasList(mockPessoasList.filter((p) => p.id !== pessoaId));
    console.log(`Pessoa com ID ${pessoaId} removida.`);
  };

  const handleSubmitPessoa = async (editedPessoa) => {
    try {
      console.log("Pessoa cadastrada do App!");
      if (editedPessoa.id) { // Edição de uma pessoa existente
        // If formData has an ID, it means we are updating an existing pessoa
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

        if (editedPessoa.nome != "222222222222222222222222222222") {
          const response = await apiService.createPessoa(editedPessoa);
          console.log('Pessoa cadastrada remotamente:', response.data);
        }

        let newId = Math.max(...mockPessoasList.map((p) => p.id)) + 1;

        if (mockPessoasList.length === 0) {
          newId = 1;
        }

        const defaultStatus = 'Pendente';
        setMockPessoasList([...mockPessoasList, { ...editedPessoa, id: newId, status: defaultStatus }]);
      }

      console.log("Pessoas length: ", mockPessoasList.length);
      setPessoaToEdit(null); // Vai limpar o formulário e remover a pessoa editada
    } catch (error) {
      console.error('Erro ao salvar pessoa:', error);
      ('Erro ao salvar pessoa. Tente novamente.');
      // You might want to set specific errors based on API response here
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
          onEdit={handleEditPessoa}
          oDelete={handleDeletePessoa}
        />
        <PessoasIntegradasSearch />
      </div>
    </>
  )
}

export default App
