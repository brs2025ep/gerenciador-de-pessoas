import React, { useState, useEffect } from 'react';
import { RiResetLeftFill } from 'react-icons/ri';

const initialPessoaState = {
  id: null,
  nome: '',
  nascimento: '',
  cpf: '',
  email: '',
  cep: '',
  rua: '',
  numero: '',
  estado: '',
  cidade: '',
};

const CadastroPage = (
  { pessoaToEdit, onSubmit }
) => {


  const [formData, setFormData] = useState(initialPessoaState);

  useEffect(() => {
    if (pessoaToEdit) {
      setFormData({
        id: pessoaToEdit.id || null,
        nome: pessoaToEdit.nome || '',
        nascimento: pessoaToEdit.nascimento || '',
        cpf: pessoaToEdit.cpf || '',
        email: pessoaToEdit.email || '',
        cep: pessoaToEdit.cep || '',
        rua: pessoaToEdit.rua || '',
        numero: pessoaToEdit.numero || '',
        cidade: pessoaToEdit.cidade || '',
        estado: pessoaToEdit.estado || '',
      });
    } else {
      setFormData(initialPessoaState);
    }
  }, [pessoaToEdit]);

  const [errors, setErrors] = useState({});

  const validate = () => {
    const newErrors = {};
    if (!nome) newErrors.nome = 'Nome é obrigatório';
    if (!/\S+@\S+\.\S+/.test(email)) newErrors.email = 'Email é inválido';
    return newErrors;
  };

  const resetCadastroForm = () => {
    setFormData(initialPessoaState);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      setErrors({});
      console.log('Form Data Submitted.');
    }
  };

  return (
    <div>
      <div className="container area-form">
        <h1>{formData.id ? 'Editar Pessoa' : 'Cadastrar Pessoa'}</h1>
        <hr className="solid"></hr>
        <form onSubmit={handleSubmit}>
          <div className="form-row-group">
            <label className="block font-medium">Nome</label>
            <input
              type="text"
              name="nome"
              value={formData.nome}
              placeholder="Digite o nome. Exemplo: Gonçalves Dias.."
              onChange={(e) => setNome(e.target.value)}
              required />
            {errors.nome && <p className="text-red-500 text-sm">{errors.nome}</p>}
          </div>

          <div className="form-row-group">
            <label className="block font-medium">Data nascimento</label>
            <input
              type="text"
              name="nascimento"
              value={formData.nascimento}
              placeholder="Digite a data de nascimento no formato dia/mês/ano. Exemplo: 25/12/1900.."
              onChange={(e) => setNascimento(e.target.value)}
            />
            {errors.nome && <p className="text-red-500 text-sm">{errors.nascimento}</p>}
          </div>

          <div className="form-row-group">
            <label className="block font-medium">CPF</label>
            <input
              type="number"
              name="cpf"
              value={formData.cpf}
              placeholder="Digite os números do CPF da pessoa..."
              onChange={(e) => setCpf(e.target.value)}
            />
            {errors.cpf && <p className="text-red-500 text-sm">{errors.cpf}</p>}
          </div>
          <div className="form-row-group">
            <label className="block font-medium">E-mail</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              placeholder="Digite o Email da pessoa..."
              onChange={(e) => setEmail(e.target.value)}
            />
            {errors.email && <p className="text-red-500 text-sm">{errors.email}</p>}
          </div>
          <fieldset>
            <legend>Endereço</legend>
            <div className="form-row-group">
              <label htmlFor="cep">Cep</label>
              <input
                type="number"
                name="cep"
                value={formData.cep}
                placeholder="Digite o CEP da Pessoa.."
                onChange={(e) => setCep(e.target.value)} />
              {errors.cep && <p className="text-red-500 text-sm">{errors.cep}</p>}
            </div>
            <div className="grouped-fields">
              <div className="form-row-group">
                <label htmlFor="rua">Rua</label>
                <input
                  type="text"
                  name="rua"
                  value={formData.rua}
                  placeholder="Digita a Rua da Pessoa.."
                  onChange={(e) => setRua(e.target.value)} />
                {errors.rua && <p className="text-red-500 text-sm">{errors.rua}</p>}
              </div>
              <div className="form-row-group">

                <label htmlFor="numero">Número</label>
                <input
                  type="number"
                  name="numero"
                  value={formData.numero}
                  placeholder="Digite o número da Residência da Pessoa.."
                  onChange={(e) => setNumero(e.target.value)} />
                {errors.numero && <p className="text-red-500 text-sm">{errors.numero}</p>}
              </div>
            </div>
            <div className="grouped-fields">
              <div className="form-row-group">
                <label htmlFor="cidade">Cidade</label>
                <input
                  type="text"
                  name="cidade"
                  value={formData.cidade}
                  placeholder="Digite o nome da Cidade da Pessoa..."
                  onChange={(e) => setCidade(e.target.value)} />
                {errors.cidade && <p className="text-red-500 text-sm">{errors.cidade}</p>}
              </div>
              <div className="form-row-group">
                <label htmlFor="estado">Estado</label>
                <input
                  type="text"
                  name="estado"
                  value={formData.estado}
                  placeholder="Digite o nome do Estado da Pessoa..."
                  onChange={(e) => setEstado(e.target.value)} />
                {errors.estado && <p className="text-red-500 text-sm">{errors.estado}</p>}
              </div>
            </div>
          </fieldset>
          <button
            type="submit"
          >
            {formData.id ? 'Salvar Mudanças' : 'Salvar'}
          </button>


          {formData.id && (
            <> {/* Fragmento para agrupar o botão e o separador */}
              Cancelar<RiResetLeftFill
                className="action-icon"
                onClick={() => resetCadastroForm()}
                title="Cancelar a edição"
              /> 
            </>
          )}

        </form>
      </div>
    </div>

  );
};

export default CadastroPage;