import { useState } from 'react'
import './App.css'
import './styles.css';
import CadastroPage from './CadastroPage'
import PessoasCadastradasList from './component/PessoasCadastradasList';
import PessoasIntegradasSearch from './component/PessoasIntegradasSearch';

function App() {

  const [mockPessoasList, setMockPessoasList] = useState ([
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
  ]);

  const [pessoaToEdit, setPessoaToEdit] = useState(null);

  const handleEditPessoa = (pessoaId) => {
    const pessoa = mockPessoasList.find((p) => p.id === pessoaId);
    // TODO: Recuperar Pessoa fazendo requisição para a API
    setPessoaToEdit(pessoa);
  };

  const handleSubmitPessoa = (editedPessoa) => {
    if (editedPessoa.id) {
      // Edição de um worker existente
      setMockPessoasList(
        mockPessoasList.map((pessoa) =>
          pessoa.id === editedPessoa.id ? editedPessoa : pessoa
        )
      );
    } else {
      const newId = Math.max(...mockPessoasList.map((p) => p.id)) + 1;
      setMockPessoasList([...mockPessoasList, { ...editedPessoa, id: newId }]);
    }
    setWorkerToEdit(null); // Limpa o workerToEdit após a submissão
  };

  return (
    <>
      <div className="container">
        <CadastroPage
        pessoaToEdit={pessoaToEdit}
        onSubmit={handleSubmitPessoa}
        />
        <PessoasCadastradasList
          pessoas={mockPessoasList}
          onEdit={handleEditPessoa}
        />
        <PessoasIntegradasSearch />
      </div>
    </>
  )
}

export default App
