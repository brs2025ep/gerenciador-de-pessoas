import React from 'react';


const pessoaConsultadaMock = {
  nome: 'John Doe',
  nascimento: '01/01/2000',
  status: 'Aprovado',
  dataInclusao: '01/01/2001 12:00:00',
  dataUltimaAlteracao: '02/02/2002 12:00:00',
};

function PessoasIntegradasSearch() {
  return (
    <div className="container area-search">
      <div className="area-search-header">Consultar Pessoas Integradas
        <hr className="solid"></hr>
      </div>
      <div className="details-container-grid"> {/* Este será o nosso contêiner Grid */}

        <div className='details-text-area'>
          <label htmlFor="cpfConsulta">CPF</label>
          <input type="text" id="cpfConsulta" name="cpfConsulta" placeholder="Digite o CPF.." />
        </div>
        <div>
          <button
            type="submit"
          >
            Consultar
          </button>
        </div>

        {/* Linha 1: Nome */}
        <span className="details-label">Nome:</span>
        <span className="details-value">{pessoaConsultadaMock.nome}</span>

        {/* Linha 2: Nascimento */}
        <span className="details-label">Nascimento:</span>
        <span className="details-value">{pessoaConsultadaMock.nascimento}</span>

        {/* Linha 3: Status */}
        <span className="details-label">Status:</span>
        <span className="details-value">{pessoaConsultadaMock.status}</span>

        {/* Linha 4: Data/Hora da Inclusão */}
        <span className="details-label">Data/Hora da Inclusão:</span>
        <span className="details-value">{pessoaConsultadaMock.dataInclusao}</span>

        {/* Linha 5: Data/Hora da Última alteração */}
        <span className="details-label">Data/Hora da Última alteração:</span>
        <span className="details-value">{pessoaConsultadaMock.dataUltimaAlteracao}</span>

      </div>

    </div>
  );
}

export default PessoasIntegradasSearch;