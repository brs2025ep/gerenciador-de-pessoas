import { MdEdit } from "react-icons/md";
import { TiDelete } from "react-icons/ti";
import { FiRefreshCw } from "react-icons/fi";


const formatCpf = (cpf) => {
  const cleanCpf = cpf.replace(/\D/g, '');
  return cleanCpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
};

function PessoasCadastradasList(
  { pessoas, onEdit }
) {

  const handleActionClick = (actionType, pessoaId) => {
    console.log(`Teste! Ação: ${actionType}, ID da Pessoa: ${pessoaId}`);
  };

  return (
    <div className="area-lists">
      <h2>Pessoas Cadastradas</h2>
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
                  onClick={() => handleActionClick('Remover', pessoa.id)}
                  title="Remover"
                />
                
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default PessoasCadastradasList;