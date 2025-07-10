import { useState } from 'react';
import { MdEdit } from "react-icons/md";
import { TiDelete } from "react-icons/ti";
import { FiRefreshCw } from "react-icons/fi";


const formatCpf = (cpf) => {
  const cleanCpf = cpf.replace(/\D/g, '');
  return cleanCpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
};

function PessoasCadastradasList(
  { pessoas, onEdit, onDelete, onIntegrar }
) {
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [pessoaToDeleteId, setPessoaToDeleteId] = useState(null);

  const handleDeleteClick = (pessoaId) => {
    console.log("Iniciada ação para remover a pessoa de id:", pessoaId);

    setPessoaToDeleteId(pessoaId);
    setShowConfirmModal(true);
  }

  const handleIntegrarClick = (pessoaId) => {
    console.log(`O botão para integrar pessoa foi acionado, ID da Pessoa, ${pessoaId}`);
    if (onIntegrar) {
      onIntegrar(pessoaId);
    }
  }

  const confirmDelete = () => {
    if (pessoaToDeleteId !== null) {
      console.log("Delete pessoa with id:", pessoaToDeleteId);
      if (onDelete) {
        onDelete(pessoaToDeleteId);
      }
    }
    setShowConfirmModal(false);
    setPessoaToDeleteId(null);
  };

  const cancelDelete = () => {
    setShowConfirmModal(false);
    setPessoaToDeleteId(null);
  };

  return (
    <div className="area-lists">
      <h2>Pessoas Cadastradas</h2>

      {pessoas.length === 0 ? (
        <p className="text-center text-gray-600">Nenhuma pessoa cadastrada. Clique em "Carregar Pessoas Mockadas" ou "Nova Pessoa" para começar.</p>
      ) : (
        <div>
          <table >
            <thead>
              <tr>
                <th>Nome</th>
                <th>Nascimento</th>
                <th className="text-center">CPF</th>
                <th>Cidade</th>
                <th className="text-center">Situação da Integração</th>
                <th className="text-center">Ação</th>
              </tr>
            </thead>
            <tbody>
              {pessoas.map((pessoa) => (
                <tr key={pessoa.id}>
                  <td>{pessoa.nome}</td>
                  <td>{pessoa.nascimento}</td>
                  <td className="text-center">{formatCpf(pessoa.cpf)}</td>
                  <td>{pessoa.cidade} / {pessoa.estado}</td>
                  <td className="text-center">{pessoa.status}</td>
                  <td className="text-center">
                    <MdEdit
                      className="action-icon"
                      onClick={() => onEdit(pessoa.id)}
                      title="Editar Pessoa"
                    />
                    {' '}

                    {pessoa.status != 'Sucesso' && (
                      <>
                        <FiRefreshCw
                          className="action-icon"
                          onClick={() => handleIntegrarClick(pessoa.id)}
                          title="Integrar"
                        />
                        {' '}
                      </>
                    )}

                    <TiDelete
                      className="action-icon"
                      onClick={() => handleDeleteClick(pessoa.id)}
                      title="Remover"
                    />

                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {showConfirmModal && (
        <div>
          <div>
            <h3>Confirmar Exclusão</h3>
            <p>Tem certeza de que deseja excluir esta pessoa?</p>
            <div>
              <button
                onClick={cancelDelete}
              >
                Cancelar
              </button>
              <button
                onClick={confirmDelete}
              >
                Confirmar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>





  );


}

export default PessoasCadastradasList;