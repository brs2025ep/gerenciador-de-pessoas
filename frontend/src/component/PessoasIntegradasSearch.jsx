import { useState, useEffect } from 'react';

const pessoaConsultadaMock = {
  id: null,
  nome: 'John Doe',
  nascimento: '01/01/2000',
  status: 'Aprovado',
  dataInclusao: '01/01/2001 12:00:00',
  dataUltimaAlteracao: '02/02/2002 12:00:00',
};

const PessoasIntegradasSearch = (
  { retrievedPessoaIntegrada, onSearch }
) => {
  const [searchData, setSearchData] = useState(pessoaConsultadaMock);
  const [cpfConsulta, setCpfConsulta] = useState('');

  useEffect(() => {
    if (retrievedPessoaIntegrada) {
      setFormData({
        id: retrievedPessoaIntegrada.id || null,
        nome: retrievedPessoaIntegrada.nome || '',
        nascimento: retrievedPessoaIntegrada.nascimento || '',
        status: retrievedPessoaIntegrada.status || '',
        dataInclusao: retrievedPessoaIntegrada.dataInclusao || '',
        dataUltimaAlteracao: retrievedPessoaIntegrada.dataUltimaAlteracao || '',
      });
    } else {
      setSearchData(pessoaConsultadaMock);
    }
  }, [retrievedPessoaIntegrada]);

  const handleCpfChange = (e) => {
    const digitsOnly = e.target.value.replace(/\D/g, '');
    const limitedDigits = digitsOnly.substring(0, 11);
    setCpfConsulta(limitedDigits);
  };

  const handleSearchClick = () => {
    if (onSearch) {
      onSearch(cpfConsulta);
    }
  };

  return (
    <div className="container area-search">
      <div className="area-search-header">Consultar Pessoas Integradas
        <hr className="solid"></hr>
      </div>
      <div className="details-container-grid">

        <div className='details-text-area'>
          <label htmlFor="cpfConsulta">CPF</label>
          <input type="text" id="cpfConsulta"
            name="cpfConsulta"
            placeholder="Digite o CPF.."
            value={cpfConsulta}
            onChange={handleCpfChange}
          />
        </div>
        <div>
          <button
            onClick={handleSearchClick}
          >
            Consultar
          </button>
        </div>
        <span className="details-label">Nome:</span>
        <span className="details-value">{searchData.nome}</span>
        <span className="details-label">Nascimento:</span>
        <span className="details-value">{searchData.nascimento}</span>
        <span className="details-label">Status:</span>
        <span className="details-value">{searchData.status}</span>
        <span className="details-label">Data/Hora da Inclusão:</span>
        <span className="details-value">{searchData.dataInclusao}</span>
        <span className="details-label">Data/Hora da Última alteração:</span>
        <span className="details-value">{searchData.dataUltimaAlteracao}</span>
      </div>

    </div>
  );
}

export default PessoasIntegradasSearch;