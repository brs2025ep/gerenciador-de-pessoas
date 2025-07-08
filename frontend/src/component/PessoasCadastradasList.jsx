import React, { useState } from 'react'; // Importa useState
import { MdEdit } from "react-icons/md";
import { TiDelete } from "react-icons/ti";
import { FiRefreshCw } from "react-icons/fi";


const formatCpf = (cpf) => {
  const cleanCpf = cpf.replace(/\D/g, '');
  return cleanCpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
};

function PessoasCadastradasList(
  { pessoas, onEdit, onDelete }
) {

  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [pessoaToDeleteId, setPessoaToDeleteId] = useState(null);

  const handleActionClick = (actionType, pessoaId) => {
    console.log(`Teste! Ação: ${actionType}, ID da Pessoa: ${pessoaId}`);
  };

  const handleDeleteClick = (pessoaId) => {
    console.log("Iniciada ação para remover a pessoa de id:", pessoaId);

    setPessoaToDeleteId(pessoaId); // Armazena o ID da pessoa
    setShowConfirmModal(true);
  }

  const confirmDelete = () => {
    if (pessoaToDeleteId !== null) {
      console.log("Delete pessoa with id:", pessoaToDeleteId);
      // Chama a função onDelete passada via props para o componente pai
      if (onDelete) {
        onDelete(pessoaToDeleteId);
      }
    }
    setShowConfirmModal(false); // Fecha o modal
    setPessoaToDeleteId(null); // Limpa o ID
  };

  const cancelDelete = () => {
    setShowConfirmModal(false); // Fecha o modal
    setPessoaToDeleteId(null); // Limpa o ID
  };

  return (
    <div className="area-lists">
      <h2>Pessoas Cadastradas</h2>

      {pessoas.length === 0 ? (
        <p className="text-center text-gray-600">Nenhuma pessoa cadastrada. Clique em "Carregar Pessoas Mockadas" ou "Nova Pessoa" para começar.</p>
      ): (
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
                {/* Botão Editar */}
                <MdEdit
                  className="action-icon"
                  onClick={() => onEdit(pessoa.id)}
                  title="Editar Pessoa"
                />
                {' '} {/* Espaçamento visual entre os botões */}
                {/* Botão Integrar - Oculto para 'Sucesso' */}

                {pessoa.status != 'Sucesso' && (
                  <> {/* Fragmento para agrupar o botão e o separador */}
                    <FiRefreshCw
                      className="action-icon"
                      onClick={() => handleActionClick('Integrar', pessoa.id)}
                      title="Integrar"
                    />
                    {' '}
                  </>
                )}

                {/* Botão Remover */}
                <TiDelete
                  className="action-icon" // Classe extra para remover
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
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-xl max-w-sm w-full">
            <h3 className="text-lg font-bold mb-4">Confirmar Exclusão</h3>
            <p className="mb-6">Tem certeza de que deseja excluir esta pessoa?</p>
            <div className="flex justify-end space-x-4">
              <button
                onClick={cancelDelete}
                className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400"
              >
                Cancelar
              </button>
              <button
                onClick={confirmDelete}
                className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
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